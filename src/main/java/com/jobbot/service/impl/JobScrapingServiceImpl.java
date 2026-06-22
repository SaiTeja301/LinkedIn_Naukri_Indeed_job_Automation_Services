package com.jobbot.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobbot.automation.LinkedInLoginBot;
import com.jobbot.dto.JobDto;
import com.jobbot.dto.UserDto;
import com.jobbot.entity.JobEntity;
import com.jobbot.exceptions.InvalideUserExeption;
import com.jobbot.mapper.EntityMapper;
import com.jobbot.repository.JobRepository;
import com.jobbot.service.JobScrapingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link JobScrapingService} for LinkedIn scraping and persistence.
 */
@Service
public class JobScrapingServiceImpl implements JobScrapingService {

    private static final Logger log = LoggerFactory.getLogger(JobScrapingServiceImpl.class);

    private final LinkedInLoginBot linkedInLoginBot;
    private final JobRepository jobRepository;

    private String linkedinUserNameorEmail;
    private String linkedinPassword;

    @Autowired
    public UserServiceImpl userService;

    public JobScrapingServiceImpl(LinkedInLoginBot linkedInLoginBot, JobRepository jobRepository) {
        this.linkedInLoginBot = linkedInLoginBot;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional
    public List<JobDto> scrapeAndSaveJobs(String linkedinUserEmail) throws InvalideUserExeption {
        try {
            // 1. Delegate scraping to the Bot (returns DTOs)
            linkedinLogin(linkedinUserEmail);
            List<JobDto> scrapedJobDtos = linkedInLoginBot.scrapeJobs();
            scrapedJobDtos.forEach(job -> job.setPlatform("LinkedIn"));
            List<JobDto> savedJobDtos = SaveJobs(scrapedJobDtos);
            return savedJobDtos;
        } catch (Exception e) {
            log.error("Error during scraping: {}", e.getMessage(), e);
            throw new InvalideUserExeption("Error during scraping: " + e.getMessage());
        }
    }

    @Override
    public List<JobDto> scrapeAndSaveJobs(String linkedinUserEmail, String title, Integer timeHours)
            throws InvalideUserExeption {
        try {
            // 1. Delegate scraping to the Bot (returns DTOs)
            linkedinLogin(linkedinUserEmail);
            List<JobDto> scrapedJobDtos = linkedInLoginBot.scrapeLatestJobsByTitleandTime(title, timeHours);
            scrapedJobDtos.forEach(job -> job.setPlatform("LinkedIn"));
            List<JobDto> savedJobDtos = SaveJobs(scrapedJobDtos);
            return savedJobDtos;
        } catch (Exception e) {
            log.error("Error during scraping: {}", e.getMessage(), e);
            throw new InvalideUserExeption("Error during scraping: " + e.getMessage());
        }
    }
 
    @Override
    public void linkedinLogin(String linkedinUserEmail) throws InvalideUserExeption {
        log.info("Starting Job Scraping Service...");
        try {
            UserDto userObj = userService.getUserByEmail(linkedinUserEmail);
            if (userObj != null) {
                String dummyValue = userObj.getEmail();
                if (dummyValue.equalsIgnoreCase(linkedinUserEmail)) {
                    linkedinUserNameorEmail = dummyValue;
                    linkedinPassword = userObj.getEncryptedPassword();
                } else {
                    log.warn("User Email mismatch: {}", linkedinUserEmail);
                    throw new InvalideUserExeption("User Email mismatch: " + linkedinUserEmail);
                }
            } else {
                log.warn("User not found with email: {}", linkedinUserEmail);
                throw new InvalideUserExeption("User not found with email: " + linkedinUserEmail);
            }
            log.info("Logging in to LinkedIn...");
            linkedInLoginBot.linkedinLogin(linkedinUserNameorEmail.trim(), linkedinPassword.trim());
        } catch (NoSuchElementException nse) {
            log.error("Login failed, element not found: {}", nse.getMessage(), nse);
            throw new InvalideUserExeption("Login failed, element not found: " + nse.getMessage());
        }
    }

    @Override
    @Transactional
    public List<JobDto> SaveJobs(List<JobDto> scrapedJobDtos) throws InvalideUserExeption {
        if (!scrapedJobDtos.isEmpty()) {
            log.info("Processing {} scraped jobs.", scrapedJobDtos.size());

            // Step 1: Build HashMap to track and detect duplicates WITHIN the current scraped batch
            HashMap<String, JobDto> jobMap = new HashMap<>();
            Set<String> inBatchDuplicates = new HashSet<>();

            for (JobDto jobDto : scrapedJobDtos) {
                String key = jobDto.getCompany() + "|" + jobDto.getTitle() + "|" + jobDto.getJobUrl();

                if (jobMap.containsKey(key)) {
                    log.info("In-batch duplicate detected: {} at {}", jobDto.getTitle(), jobDto.getCompany());
                    inBatchDuplicates.add(jobDto.getJobUrl());
                } else {
                    jobMap.put(key, jobDto);
                }
            }

            // Step 2: Collect jobs to remove (both in-batch duplicates and database duplicates)
            List<JobDto> duplicatesToRemove = new ArrayList<>();
            List<JobEntity> updatedEntities = new ArrayList<>();

            // Check for duplicates in database
            for (JobDto scrapedJobDto : scrapedJobDtos) {
                try {
                    // Skip if already marked as in-batch duplicate
                    if (inBatchDuplicates.contains(scrapedJobDto.getJobUrl())) {
                        duplicatesToRemove.add(scrapedJobDto);
                        continue;
                    }

                    // Use findFirstByJobUrlOrderByIdAsc to get only one result
                    JobEntity existingJob = jobRepository.findFirstByJobUrlOrderByIdAsc(scrapedJobDto.getJobUrl())
                            .orElse(null);

                    if (existingJob != null) {
                        log.info("Duplicate job found in DB, skipping: {} at {} (URL: {})",
                                scrapedJobDto.getTitle(), scrapedJobDto.getCompany(), scrapedJobDto.getJobUrl());
                        existingJob.setJob_applyed_count_status(scrapedJobDto.getJob_applyed_count_status());
                        existingJob.setJob_posted(scrapedJobDto.getJob_posted());
                        updatedEntities.add(jobRepository.save(existingJob));
                        log.info("Updated existing job with latest status: {} - {} - {}",
                                existingJob.getId(), existingJob.getCompany(), existingJob.getTitle());
                        duplicatesToRemove.add(scrapedJobDto);
                    }
                } catch (Exception e) {
                    log.error("Error checking existing job for: {} at {} - {}",
                            scrapedJobDto.getTitle(), scrapedJobDto.getCompany(), e.getMessage());
                }
            }

            // Step 3: Remove all duplicates at once
            scrapedJobDtos.removeAll(duplicatesToRemove);
            log.info("Removed {} duplicate jobs ({} in-batch, {} from DB). Remaining: {}",
                    duplicatesToRemove.size(), inBatchDuplicates.size(),
                    (duplicatesToRemove.size() - inBatchDuplicates.size()), scrapedJobDtos.size());

            List<JobEntity> jobEntities = scrapedJobDtos.stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());

            List<JobEntity> savedEntities = jobRepository.saveAllAndFlush(jobEntities);

            if (savedEntities.isEmpty()) {
                log.warn("No new jobs were saved to the database.");
            } else {
                log.info("Successfully saved {} jobs to database.", savedEntities.size());
                savedEntities.forEach(job -> log.info("Saved Job - Title: {}, Generated ID: {}",
                        job.getTitle(), job.getId()));
            }

            List<JobDto> allJobDtos = savedEntities.stream()
                    .map(EntityMapper::toDto)
                    .collect(Collectors.toList());

            return allJobDtos;
        } else {
            log.info("No jobs scraped.");
            return Collections.emptyList();
        }
    }

    private JobEntity toEntity(JobDto dto) {
        JobEntity entity = new JobEntity();
        entity.setTitle(dto.getTitle());
        entity.setCompany(dto.getCompany());
        entity.setLocation(dto.getLocation());
        entity.setJobUrl(dto.getJobUrl());
        entity.setPlatform(dto.getPlatform());
        entity.setJob_posted(dto.getJob_posted());
        entity.setJob_applyed_count_status(dto.getJob_applyed_count_status());
        entity.setDescription(dto.getDescription());
        entity.setApplied(false); // Default value for new scrapes
        return entity;
    }
}
