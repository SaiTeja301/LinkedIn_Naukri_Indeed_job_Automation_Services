package com.jobbot.service;

import com.jobbot.dto.JobDto;
import com.jobbot.exceptions.InvalideUserExeption;

import java.util.List;

public interface JobService {
    List<JobDto> getAllJobs();

    JobDto getJobById(Long id);

    void deleteJob(Long id);

    void deleteJobList(List<Long> ids);

    List<JobDto> scrapeJobs(String linkedinUserEmailorUserName) throws InvalideUserExeption;
    
    List<JobDto> scrapeJobs(String linkedinUserEmailorUserName, String Title, Integer timeHours) throws InvalideUserExeption;

    String applyToJob(Long id);

    String applyToJobs(List<Long> ids);
}
