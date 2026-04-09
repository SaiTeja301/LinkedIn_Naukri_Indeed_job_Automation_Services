package com.jobbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.client.WebClient;

@Configuration
 // Exclude Gemini AI configuration from test profile
public class GeminiConfig {

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent}")
    private String apiUrl;

    @Bean
    public WebClient geminiWebClient() {
        String apiKey = System.getenv("GEMINI_API_KEY");
        return WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }
}
