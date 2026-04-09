package com.jobbot.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Selenium WebDriver configuration.
 * Headless mode is controlled by the 'selenium.headless' property in application.properties.
 * The WebDriver bean is lazily initialized so it is only created when first needed.
 */
@Configuration
public class SeleniumConfig {

    /** Toggle headless Chrome execution — set true for CI/server environments. */
    @Value("${selenium.headless:false}")
    private boolean headless;

    @Bean
    @Lazy
    public WebDriver webDriver() {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            // Use the modern --headless flag (Chrome 112+)
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // Mask WebDriver presence to reduce bot-detection fingerprinting
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        return new ChromeDriver(options);
    }
}
