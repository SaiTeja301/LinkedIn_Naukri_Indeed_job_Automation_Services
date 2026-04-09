package com.jobbot.service.impl;

import com.jobbot.dto.ResumeDto;
import com.jobbot.entity.ResumeEntity;
import com.jobbot.entity.UserEntity;
import com.jobbot.mapper.EntityMapper;
import com.jobbot.repository.ResumeRepository;
import com.jobbot.repository.UserRepository;
import com.jobbot.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ResumeServiceImpl(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResumeDto updateResume(MultipartFile file, Long userId) throws IOException {
        String content = new String(file.getBytes());

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ResumeEntity resume = new ResumeEntity();
        resume.setUser(user);
        resume.setContent(content);
        resume.setVersion("v" + System.currentTimeMillis());

        return EntityMapper.toDto(resumeRepository.save(resume));
    }
}
