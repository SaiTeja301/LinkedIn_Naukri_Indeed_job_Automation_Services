package com.jobbot.service;

import com.jobbot.dto.JobDto;
import com.jobbot.exceptions.InvalideUserExeption;
import java.util.List;

public interface JobScrapingService {
    List<JobDto> scrapeAndSaveJobs(String linkedinUserEmail) throws InvalideUserExeption;
    List<JobDto> scrapeAndSaveJobs(String linkedinUserEmail,String Title, Integer timeHours) throws InvalideUserExeption;
    void linkedinLogin(String linkedinUserEmail) throws InvalideUserExeption, InterruptedException;
    List<JobDto> SaveJobs(List<JobDto> scrapedJobDtos) throws InvalideUserExeption;
    
}
