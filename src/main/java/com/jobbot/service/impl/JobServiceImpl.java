package com.jobbot.service.impl;

import com.jobbot.dto.JobDto;
import com.jobbot.entity.JobEntity;
import com.jobbot.exceptions.InvalideUserExeption;
import com.jobbot.mapper.EntityMapper;
import com.jobbot.repository.JobRepository;
import com.jobbot.service.JobScrapingService;
import com.jobbot.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobScrapingService jobScrapingService;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, JobScrapingService jobScrapingService) {
        this.jobRepository = jobRepository;
        this.jobScrapingService = jobScrapingService;
    }

    @Override
    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(EntityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobDto getJobById(Long id) {
        return jobRepository.findById(id)
                .map(EntityMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public void deleteJobList(List<Long> ids) {
        jobRepository.deleteAllById(ids);
    }

    @Override
    public List<JobDto> scrapeJobs(String linkedinUserEmailorUserName)
            throws com.jobbot.exceptions.InvalideUserExeption {
        return jobScrapingService.scrapeAndSaveJobs(linkedinUserEmailorUserName);
    }
    
    @Override
	public List<JobDto> scrapeJobs(String linkedinUserEmailorUserName, String Title, Integer timeHours)
			throws InvalideUserExeption {
		return jobScrapingService.scrapeAndSaveJobs(linkedinUserEmailorUserName, Title, timeHours);
	}

    @Override
    public String applyToJob(Long id) {
        return jobRepository.findById(id).map(job -> {
            // Trigger Automation Logic for Single Job
            // linkedInLoginBot.applyToJob(job.getJobUrl()); // Placeholder
            return "Triggered application for job: " + job.getTitle();
        }).orElse("Job not found");
    }

    @Override
    public String applyToJobs(List<Long> ids) {
        List<JobEntity> jobs = jobRepository.findAllById(ids);
        if (jobs.isEmpty()) {
            return "No jobs found for provided IDs.";
        }
        // Trigger Automation Logic for List
        // linkedInLoginBot.applyToJobs(jobs); // Placeholder
        return "Triggered application for " + jobs.size() + " jobs.";
    }

	
}
