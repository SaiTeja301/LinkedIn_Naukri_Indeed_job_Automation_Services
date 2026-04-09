package com.jobbot.service.impl;

import com.jobbot.dto.AiResponseDto;
import com.jobbot.entity.AiResponseEntity;
import com.jobbot.entity.UserEntity;
import com.jobbot.repository.AiResponseRepository;
import com.jobbot.repository.JobApplicationRepository;
import com.jobbot.repository.JobRepository;
import com.jobbot.repository.ResumeRepository;
import com.jobbot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiAgentServiceImplTest {

    @Mock
    private WebClient geminiWebClient;

    @Mock
    private AiResponseRepository aiResponseRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private AiAgentServiceImpl aiAgentService;

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void testAskAgent() {
        when(jobRepository.findAll()).thenReturn(Collections.emptyList());
        when(jobApplicationRepository.findAll()).thenReturn(Collections.emptyList());

        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(resumeRepository.findAll()).thenReturn(Collections.emptyList());

        // Mock WebClient deep chain
        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(geminiWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        Map responseMap = Map.of("data", "Sample AI Response");
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseMap));

        AiResponseEntity savedEntity = new AiResponseEntity();
        savedEntity.setId(100L);
        savedEntity.setResponse(responseMap.toString());
        when(aiResponseRepository.save(any())).thenReturn(savedEntity);

        Mono<AiResponseDto> resultMono = aiAgentService.askAgent("Hello Agent", 1L);

        AiResponseDto dto = resultMono.block();
        org.junit.jupiter.api.Assertions.assertNotNull(dto);
        org.junit.jupiter.api.Assertions.assertEquals(100L, dto.getId());
    }
}
