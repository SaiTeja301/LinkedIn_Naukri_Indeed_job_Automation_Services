package com.jobbot.service.impl;

import com.jobbot.dto.AiResponseDto;
import com.jobbot.mapper.EntityMapper;
import com.jobbot.repository.AiResponseRepository;
import com.jobbot.service.AiAgentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service

public class AiAgentServiceImpl implements AiAgentService {

        private final WebClient geminiWebClient;
        private final AiResponseRepository aiResponseRepository;
        private final com.jobbot.repository.JobRepository jobRepository;
        private final com.jobbot.repository.ResumeRepository resumeRepository;
        private final com.jobbot.repository.UserRepository userRepository;
        private final com.jobbot.repository.JobApplicationRepository jobApplicationRepository;

        @Autowired
        public AiAgentServiceImpl(WebClient geminiWebClient, AiResponseRepository aiResponseRepository,
                        com.jobbot.repository.JobRepository jobRepository,
                        com.jobbot.repository.ResumeRepository resumeRepository,
                        com.jobbot.repository.UserRepository userRepository,
                        com.jobbot.repository.JobApplicationRepository jobApplicationRepository) {
                this.geminiWebClient = geminiWebClient;
                this.aiResponseRepository = aiResponseRepository;
                this.jobRepository = jobRepository;
                this.resumeRepository = resumeRepository;
                this.userRepository = userRepository;
                this.jobApplicationRepository = jobApplicationRepository;
        }

        @Override
        public Mono<AiResponseDto> askAgent(String prompt, Long userId) {
                // 1. Fetch Data
                java.util.List<com.jobbot.entity.JobEntity> jobs = jobRepository.findAll();
                java.util.List<com.jobbot.entity.JobApplicationEntity> applications = jobApplicationRepository
                                .findAll();

                // 2. Create Context String
                StringBuilder context = new StringBuilder();
                context.append("You are a specialized Job Automation Assistant.\n");
                context.append(
                                "Your role is to help users with job searches, resume analysis, and career advice based on the data provided.\n");
                context.append(
                                "Strictly refuse to answer questions unrelated to jobs, resumes, or career development (e.g., weather, movies, jokes).\n");
                context.append("Here is the current job data in the database:\n");
                context.append("--------------------------------------------------\n");

                if (jobs != null && !jobs.isEmpty()) {
                        jobs.stream().limit(20).forEach(j -> {
                                context.append(String.format(
                                                "ID: %s, Title: %s, Company: %s, Location: %s, Applied: %s\n",
                                                j.getId(), j.getTitle(), j.getCompany(), j.getLocation(),
                                                j.isApplied()));
                        });
                } else {
                        context.append("No jobs found in the database.\n");
                }

                context.append("--------------------------------------------------\n");
                context.append("Current Job Applications Status:\n");
                if (applications != null && !applications.isEmpty()) {
                        applications.stream().limit(20).forEach(app -> {
                                context.append(String.format(
                                                "App ID: %s, Job: %s, Status: %s, Applied Flag: %s\n",
                                                app.getId(), app.getJob().getTitle(), app.getStatus(),
                                                app.isJobApplied()));
                        });
                } else {
                        context.append("No active applications found.\n");
                }

                if (userId != null) {
                        context.append("--------------------------------------------------\n");
                        context.append("User Context:\n");
                        com.jobbot.entity.UserEntity user = userRepository.findById(userId)
                                        .orElseThrow(() -> new RuntimeException("User not found"));
                        context.append("Profile: ").append(user.toString())
                                        .append("\n");

                        resumeRepository.findAll().stream()
                                        .filter(r -> r.getUser().getId().equals(userId))
                                        .findFirst()
                                        .ifPresent(r -> context.append("Latest Resume Content: ").append(r.getContent())
                                                        .append("\n"));
                }

                context.append("--------------------------------------------------\n");
                context.append(
                                "Answer the following question based ONLY on the data provided above and your expertise in recruitment.\n");
                context.append("User Question: ").append(prompt);

                // Construct the Gemini API request body
                Map<String, Object> requestBody = Map.of(
                                "contents", new Object[] {
                                                Map.of("parts", new Object[] {
                                                                Map.of("text", context.toString())
                                                })
                                });

                return geminiWebClient.post()
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(Map.class)
                                .map(response -> {
                                        String responseText = response.toString();

                                        com.jobbot.entity.AiResponseEntity entity = new com.jobbot.entity.AiResponseEntity();
                                        entity.setPrompt(prompt);
                                        entity.setResponse(responseText);
                                        var savedEntity = aiResponseRepository.save(entity);

                                        return EntityMapper.toDto(savedEntity);
                                });
        }

        @Override
        public Mono<AiResponseDto> analyzeResume(Long userId) {
                String resumeContent = resumeRepository.findAll().stream()
                                .filter(r -> r.getUser().getId().equals(userId))
                                .map(com.jobbot.entity.ResumeEntity::getContent)
                                .findFirst()
                                .orElse("No resume found.");

                String jobs = jobRepository.findAll().stream()
                                .limit(5)
                                .map(j -> j.getTitle() + ": " + j.getDescription())
                                .collect(java.util.stream.Collectors.joining("\n---\n"));

                String prompt = String.format(
                                "Analyze this resume and suggest improvements based on these jobs:\nResume: %s \n\nJobs:\n%s",
                                resumeContent, jobs);

                return askAgent(prompt, userId);
        }
}
