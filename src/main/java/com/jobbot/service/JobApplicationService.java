package com.jobbot.service;

public interface JobApplicationService {
    String updateApplicationStatus(Long applicationId, boolean applied);
}
