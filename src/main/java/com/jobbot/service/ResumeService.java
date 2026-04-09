package com.jobbot.service;

import com.jobbot.dto.ResumeDto;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {
    ResumeDto updateResume(MultipartFile file, Long userId) throws Exception;
}
