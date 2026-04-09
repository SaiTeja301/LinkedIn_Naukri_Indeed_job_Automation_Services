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

@Service

public class JobScrapingServiceImpl implements JobScrapingService {

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
            System.out.println("Error during scraping: " + e.getMessage());
            throw new InvalideUserExeption("Error during scraping: " + e.getMessage());
        }
    }
    @Override
	public List<JobDto> scrapeAndSaveJobs(String linkedinUserEmail, String Title, Integer timeHours)
			throws InvalideUserExeption {
    	  try {
              // 1. Delegate scraping to the Bot (returns DTOs)
              linkedinLogin(linkedinUserEmail);
              List<JobDto> scrapedJobDtos = linkedInLoginBot.scrapeLatestJobsByTitleandTime(Title, timeHours);
              scrapedJobDtos.forEach(job -> job.setPlatform("LinkedIn"));
              List<JobDto> savedJobDtos = SaveJobs(scrapedJobDtos);
              return savedJobDtos;
          } catch (Exception e) {
              System.out.println("Error during scraping: " + e.getMessage());
              throw new InvalideUserExeption("Error during scraping: " + e.getMessage());
          }
	}

    @Override
    public void linkedinLogin(String linkedinUserEmail) throws InvalideUserExeption, InterruptedException {
        System.out.println("Starting Job Scraping Service...");
        try {

            UserDto userObj = userService.getUserByEmail(linkedinUserEmail);
            if (userObj != null) {
                // if User Founds'
                String dummyValue = userObj.getEmail();
                if (dummyValue.equalsIgnoreCase(linkedinUserEmail)) {
                    linkedinUserNameorEmail = dummyValue;
                    linkedinPassword = userObj.getEncryptedPassword();
                } else {
                    System.out.println("User Email mismatch: " + linkedinUserEmail);
                    throw new InvalideUserExeption("User Email mismatch: " + linkedinUserEmail);
                }
            } else {
                System.out.println("User not found with email: " + linkedinUserEmail);
                throw new InvalideUserExeption("User not found with email: " + linkedinUserEmail);
            }
            System.out.println("Logging in to LinkedIn...");
            linkedInLoginBot.LinkedInlogin(linkedinUserNameorEmail.trim(), linkedinPassword.trim());
        } catch (NoSuchElementException nse) {
            System.out.println("Login failed, element not found: " + nse.getMessage());
            throw new InvalideUserExeption("Login failed, element not found: " + nse.getMessage());
        }
    }

    @Override
    @Transactional
    public List<JobDto> SaveJobs(List<JobDto> scrapedJobDtos) throws InvalideUserExeption {
        if (!scrapedJobDtos.isEmpty()) {
            System.out.println("Processing " + scrapedJobDtos.size() + " scraped jobs.");

            // Step 1: Build HashMap to track and detect duplicates WITHIN the current
            // scraped batch
            HashMap<String, JobDto> jobMap = new HashMap<>();
            Set<String> inBatchDuplicates = new HashSet<>();

            for (JobDto jobDto : scrapedJobDtos) {
                String key = jobDto.getCompany() + "|" + jobDto.getTitle() + "|" + jobDto.getJobUrl();

                if (jobMap.containsKey(key)) {
                    System.out.println(
                            "In-batch duplicate detected: " + jobDto.getTitle() + " at " + jobDto.getCompany());
                    inBatchDuplicates.add(jobDto.getJobUrl());
                } else {
                    jobMap.put(key, jobDto);
                }
            }

            // Step 2: Collect jobs to remove (both in-batch duplicates and database
            // duplicates)
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

                    // Use findFirstByJobUrlOrderByIdAsc to get only one result (handles multiple
                    // results gracefully)
                    JobEntity existingJob = jobRepository.findFirstByJobUrlOrderByIdAsc(scrapedJobDto.getJobUrl())
                            .orElse(null);

                    if (existingJob != null) {
                        System.out.println("Duplicate job found in DB, skipping: " + scrapedJobDto.getTitle() + " at "
                                + scrapedJobDto.getCompany() + " (URL: " + scrapedJobDto.getJobUrl() + ")");
                        existingJob.setJob_applyed_count_status(scrapedJobDto.getJob_applyed_count_status());
                        existingJob.setJob_posted(scrapedJobDto.getJob_posted());
                        updatedEntities.add(jobRepository.save(existingJob));
                        System.out.println("Updated existing job with latest status: " + existingJob.getId() + " - "
                                + existingJob.getCompany() + " - " + existingJob.getTitle());
                        duplicatesToRemove.add(scrapedJobDto);
                    }
                } catch (Exception e) {
                    System.out.println("  Error checking existing job for: " + scrapedJobDto.getTitle() + " at "
                            + scrapedJobDto.getCompany() + " - " + e.getMessage());
                }
            }

            // Step 3: Remove all duplicates at once (fail-safe approach)
            scrapedJobDtos.removeAll(duplicatesToRemove);
            System.out
                    .println("Removed " + duplicatesToRemove.size() + " duplicate jobs (" + inBatchDuplicates.size()
                            + " in-batch, " + (duplicatesToRemove.size() - inBatchDuplicates.size())
                            + " from DB). Remaining: " + scrapedJobDtos.size());

            List<JobEntity> jobEntities = scrapedJobDtos.stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());

            // System.out.println("Mapped scraped jobs to entities (IDs are null before
            // save): " + jobEntities);
            // System.out.println("Saving " + jobEntities.size() + " jobs to database.");
            // Use saveAllAndFlush to force persistence and ID generation immediately

            List<JobEntity> savedEntities = jobRepository.saveAllAndFlush(jobEntities);

            if (savedEntities.isEmpty()) {
                System.out.println("No new jobs were saved to the database.");
            } else {
                System.out.println("Successfully saved " + savedEntities.size() + " jobs to database.");
                // Log generated IDs to confirm persistence
                savedEntities.forEach(job -> System.out
                        .println("Saved Job - Title: " + job.getTitle() + ", Generated ID: " + job.getId()));
            }

            List<JobDto> allJobDtos = new ArrayList<>();
            allJobDtos.addAll(updatedEntities.stream()
                    .map(EntityMapper::toDto)
                    .collect(Collectors.toList()));
            allJobDtos.addAll(savedEntities.stream()
                    .map(EntityMapper::toDto)
                    .collect(Collectors.toList()));

            return allJobDtos;
        } else {
            System.out.println("No jobs scraped.");
            return Collections.emptyList();
        }
    }

    private JobEntity toEntity(JobDto dto) {
        // Manual mapping to ensure we don't overwrite default values (like createdAt)
        // with nulls
        JobEntity entity = new JobEntity();
        // ID is null for new entities, let DB handle it
        entity.setTitle(dto.getTitle());
        entity.setCompany(dto.getCompany());
        entity.setLocation(dto.getLocation());
        entity.setJobUrl(dto.getJobUrl());
        entity.setPlatform(dto.getPlatform());
        entity.setJob_posted(dto.getJob_posted());
        entity.setJob_applyed_count_status(dto.getJob_applyed_count_status());
        entity.setDescription(dto.getDescription());
        entity.setApplied(false); // Default value for new scrapes

        // NOTE: We intentionally DO NOT set createdAt here.
        // JobEntity initializes it to LocalDateTime.now().
        // If we used ModelMapper, it might copy 'null' from the DTO's createdAt,
        // causing a DB error.

        return entity;
    }

	
}
