package com.jobbot.controller;

import com.jobbot.service.JobService;
import com.jobbot.exceptions.InvalideUserExeption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AutomationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JobService jobService;

    @InjectMocks
    private AutomationController automationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(automationController).build();
    }

    @Test
    void testApplyToJob() throws Exception {
        when(jobService.applyToJob(1L)).thenReturn("Success");

        mockMvc.perform(post("/auto-jobs/apply-job/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void testScrapeJobs() throws Exception {
        when(jobService.scrapeJobs("test@email.com")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/auto-jobs/linkedin-jobs/scrape")
                .param("linkedinUserEmailorUserName", "test@email.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Scraping completed. No new jobs found."));
    }
}
