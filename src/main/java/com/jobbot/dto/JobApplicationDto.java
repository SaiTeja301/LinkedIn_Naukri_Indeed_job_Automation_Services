package com.jobbot.dto;

import com.jobbot.entity.JobApplicationEntity.ApplicationStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobApplicationDto {
    private Long id;
    private Long jobId;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private boolean isJobApplied;
}
