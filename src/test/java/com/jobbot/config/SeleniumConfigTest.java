package com.jobbot.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SeleniumConfigTest {

    private SeleniumConfig seleniumConfig;

    @BeforeEach
    void setUp() {
        seleniumConfig = new SeleniumConfig();
    }

    @Test
    void testWebDriverCreation() {
        // Mock ChromeDriver construction to prevent launching real browser during unit test
        try (MockedConstruction<ChromeDriver> mockedConstruction = Mockito.mockConstruction(ChromeDriver.class)) {
            WebDriver driver = seleniumConfig.webDriver();
            
            assertNotNull(driver, "WebDriver should not be null");
            assertTrue(driver instanceof ChromeDriver, "WebDriver should be instance of ChromeDriver");
            
            // Verify our mock was constructed
            org.junit.jupiter.api.Assertions.assertEquals(1, mockedConstruction.constructed().size());
        }
    }
}
