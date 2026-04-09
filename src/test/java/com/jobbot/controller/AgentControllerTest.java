package com.jobbot.controller;

import com.jobbot.dto.AiResponseDto;
import com.jobbot.service.AiAgentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgentControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private AiAgentService aiAgentService;

    @InjectMocks
    private AgentController agentController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(agentController).build();
    }

    @Test
    void testAskAgent() {
        AiResponseDto dto = new AiResponseDto();
        dto.setId(1L);
        dto.setResponse("OK");

        when(aiAgentService.askAgent(anyString(), anyLong())).thenReturn(Mono.just(dto));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auto-jobs/agent/ask-agent")
                        .queryParam("prompt", "Hello")
                        .queryParam("userId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.response").isEqualTo("OK");
    }

    @Test
    void testAnalyzeResume() {
        AiResponseDto dto = new AiResponseDto();
        dto.setId(2L);
        dto.setResponse("Analyzed");

        when(aiAgentService.analyzeResume(anyLong())).thenReturn(Mono.just(dto));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/auto-jobs/agent/analyze-resume")
                        .queryParam("userId", 1)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.response").isEqualTo("Analyzed");
    }
}
