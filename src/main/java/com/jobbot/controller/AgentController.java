package com.jobbot.controller;

import com.jobbot.dto.AiResponseDto;
import com.jobbot.service.AiAgentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auto-jobs/agent")

public class AgentController {

    private final AiAgentService aiAgentService;

    @Autowired
    public AgentController(AiAgentService aiAgentService) {
        this.aiAgentService = aiAgentService;
    }

    @PostMapping("/ask-agent")
    public Mono<AiResponseDto> askAgent(@RequestParam String prompt, @RequestParam Long userId) {
        return aiAgentService.askAgent(prompt, userId);
    }

    @GetMapping("/analyze-resume")
    public Mono<AiResponseDto> analyzeResume(@RequestParam Long userId) {
        return aiAgentService.analyzeResume(userId);
    }
}
