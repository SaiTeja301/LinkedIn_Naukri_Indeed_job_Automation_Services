package com.Linked_jobs_Spring_boot_Automation_Services;

import com.jobbot.dto.JobDto;
import com.jobbot.exceptions.InvalideUserExeption;
import com.jobbot.service.JobScrapingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.Collections;
import java.util.List;

@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    JobScrapingService jobScrapingService() {
        return new JobScrapingService() {
            @Override
            public List<JobDto> scrapeAndSaveJobs(String linkedinUserEmail) throws InvalideUserExeption {
                // Mock implementation for tests
                return Collections.emptyList();
            }

            @Override
            public void linkedinLogin(String linkedinUserEmail) throws InvalideUserExeption, InterruptedException {
                System.out.println("Mock linkedinLogin called for: " + linkedinUserEmail);
            }

            @Override
            public List<JobDto> SaveJobs(List<JobDto> scrapedJobDtos) throws InvalideUserExeption {
                return Collections.emptyList();
            }

			@Override
			public List<JobDto> scrapeAndSaveJobs(String linkedinUserEmail, String Title, Integer timeHours)
					throws InvalideUserExeption {
				return Collections.emptyList();
			}
        };
    }
}
