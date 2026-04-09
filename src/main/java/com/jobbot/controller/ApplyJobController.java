package com.jobbot.controller;

import com.jobbot.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auto-jobs/apply")
public class ApplyJobController {

    private final JobApplicationService jobApplicationService;

    @Autowired
    public ApplyJobController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/update-job-status")
    public String updateJobStatus(@RequestParam Long applicationId, @RequestParam boolean applied) {
        return jobApplicationService.updateApplicationStatus(applicationId, applied);
    }
}
