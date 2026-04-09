package com.jobbot.controller;

import com.jobbot.dto.ResumeDto;
import com.jobbot.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auto-jobs")
public class ResumeController {

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/update-resume")
    public ResumeDto updateResume(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            return resumeService.updateResume(file, userId);
        } catch (Exception e) {
            // Handle exception appropriately, e.g., log it and return an error DTO or throw
            // a custom exception
            // For now, rethrowing as a RuntimeException for simplicity, but consider more
            // specific handling.
            throw new RuntimeException("Failed to update resume: " + e.getMessage(), e);
        }
    }
}
