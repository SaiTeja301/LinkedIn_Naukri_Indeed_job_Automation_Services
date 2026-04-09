package com.jobbot.service;

import com.jobbot.dto.AiResponseDto;
import reactor.core.publisher.Mono;

public interface AiAgentService {
        Mono<AiResponseDto> askAgent(String prompt, Long userId);

        Mono<AiResponseDto> analyzeResume(Long userId);
}
