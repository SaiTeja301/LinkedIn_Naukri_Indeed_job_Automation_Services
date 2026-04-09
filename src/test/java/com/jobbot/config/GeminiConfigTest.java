package com.jobbot.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GeminiConfigTest {

    @Test
    void testGeminiWebClient() {
        GeminiConfig config = new GeminiConfig();
        // Use reflection to set the injected value
        ReflectionTestUtils.setField(config, "apiUrl", "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent");

        WebClient webClient = config.geminiWebClient();
        assertNotNull(webClient, "WebClient should be created successfully");
    }
}
