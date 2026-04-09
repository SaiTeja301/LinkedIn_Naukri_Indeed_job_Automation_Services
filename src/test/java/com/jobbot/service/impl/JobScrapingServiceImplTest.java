package com.jobbot.service.impl;

import com.jobbot.automation.LinkedInLoginBot;
import com.jobbot.dto.JobDto;
import com.jobbot.dto.UserDto;
import com.jobbot.entity.JobEntity;
import com.jobbot.exceptions.InvalideUserExeption;
import com.jobbot.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobScrapingServiceImplTest {

    @Mock
    private LinkedInLoginBot linkedInLoginBot;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private JobScrapingServiceImpl jobScrapingService;

    @BeforeEach
    void setUp() {
        // Since userService is public in JobScrapingServiceImpl, InjectMocks should handle it,
        // but let's confirm it's injected.
        jobScrapingService.userService = userService;
    }

    @Test
    void testLinkedinLoginSuccess() throws InvalideUserExeption, InterruptedException {
        UserDto user = new UserDto();
        user.setEmail("test@email.com");
        user.setEncryptedPassword("encodedPass");

        when(userService.getUserByEmail(anyString())).thenReturn(user);

        jobScrapingService.linkedinLogin("test@email.com");

        verify(linkedInLoginBot, times(1)).LinkedInlogin("test@email.com", "encodedPass");
    }

    @Test
    void testLinkedinLoginUserNotFound() {
        when(userService.getUserByEmail(anyString())).thenReturn(null);

        assertThrows(InvalideUserExeption.class, () -> {
            jobScrapingService.linkedinLogin("unknown@email.com");
        });
    }

    @Test
    void testSaveJobsWithDuplicates() throws InvalideUserExeption {
        JobDto scrapedJob1 = new JobDto();
        scrapedJob1.setTitle("Dev");
        scrapedJob1.setCompany("Tech");
        scrapedJob1.setJobUrl("url1");
        
        JobDto scrapedJob2 = new JobDto(); // Duplicate
        scrapedJob2.setTitle("Dev");
        scrapedJob2.setCompany("Tech");
        scrapedJob2.setJobUrl("url1");

        List<JobDto> jobs = new ArrayList<>();
        jobs.add(scrapedJob1);
        jobs.add(scrapedJob2);

        lenient().when(jobRepository.findFirstByJobUrlOrderByIdAsc("url1")).thenReturn(Optional.empty());
        lenient().when(jobRepository.saveAllAndFlush(anyList())).thenReturn(Collections.emptyList());

        List<JobDto> result = jobScrapingService.SaveJobs(jobs);
        
        verify(jobRepository, times(1)).saveAllAndFlush(anyList());
    }
}
