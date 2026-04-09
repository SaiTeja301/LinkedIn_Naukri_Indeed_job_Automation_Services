package com.jobbot.automation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LinkedInLoginBotTest {

    @InjectMocks
    private LinkedInLoginBot linkedInLoginBot;

    @Test
    void testLinkedInLogin() throws InterruptedException {
        try (MockedConstruction<ChromeDriver> mockedConstruction = mockConstruction(ChromeDriver.class,
                (mockDriver, context) -> {
                    WebDriver.Options options = mock(WebDriver.Options.class);
                    WebDriver.Window window = mock(WebDriver.Window.class);
                    when(mockDriver.manage()).thenReturn(options);
                    when(options.window()).thenReturn(window);

                    WebElement element = mock(WebElement.class);
                    when(mockDriver.findElements(any(By.class))).thenReturn(Collections.singletonList(element));
                    when(mockDriver.findElement(any(By.class))).thenReturn(element);
                })) {

            linkedInLoginBot.LinkedInlogin("user@linkedin.com", "password");

            org.junit.jupiter.api.Assertions.assertEquals(1, mockedConstruction.constructed().size());
            ChromeDriver driver = mockedConstruction.constructed().get(0);

            verify(driver, times(1)).get(anyString());
            verify(driver, atLeastOnce()).findElements(any(By.class));
            verify(driver, atLeastOnce()).findElement(any(By.class));
        }
    }
    
    @Test
    void testQuitWebDriver() {
        WebDriver mockDriver = mock(WebDriver.class);
        LinkedInLoginBot bot = new LinkedInLoginBot(mockDriver);
        bot.quitwebdriver();
        verify(mockDriver, times(1)).quit();
    }
}
