package com.jobbot.automation;

import com.jobbot.utils.RateLimiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NaukriJobBotTest {

    @Mock(extraInterfaces = {JavascriptExecutor.class})
    private WebDriver driver;

    @Mock
    private WebElement webElement;

    @InjectMocks
    private NaukriJobBot naukriJobBot;

    @Test
    void testNaukriLogin() {
        // Mock RateLimiter to skip actual thread sleeps and speed up the unit test
        try (MockedStatic<RateLimiter> mockedRateLimiter = mockStatic(RateLimiter.class)) {
            
            // Mock the JavascriptExecutor behavior needed by WaitUtils.waitForPageLoad
            when(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                    .thenReturn("complete");

            // Mock Find Elements to instantly return for WebDriverWait calls
            when(driver.findElement(any(By.class))).thenReturn(webElement);
            when(webElement.isDisplayed()).thenReturn(true);
            when(webElement.isEnabled()).thenReturn(true);

            naukriJobBot.naukriLogin("test@email.com", "password");

            // Verify the driver was used to navigate
            verify(driver, times(1)).get(anyString());
            // Verify interactions on the web element
            verify(webElement, atLeastOnce()).clear();
            verify(webElement, atLeastOnce()).sendKeys(anyString());
            verify(webElement, atLeastOnce()).click();
        }
    }
}
