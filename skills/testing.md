# Testing & Quality Assurance Skills

This document details the testing methodologies, mocking libraries, and test design patterns demonstrated in the codebase.

---

## 1. Unit Testing with JUnit 5 & Mockito
* **Confidence Level**: 90% (Advanced)
* **Inferred Skills**:
  * **JUnit 5 Integration**: Standardizing tests with Jupiter annotations (`@Test`, `@ExtendWith(MockitoExtension.class)`).
  * **Dependency Mocking**: Injecting mock drivers (`@Mock WebDriver`) and testing controllers and services in isolation.
  * **Mockito Static Mocking**: Using `Mockito.mockStatic()` within try-with-resources blocks to mock static helper methods (specifically `RateLimiter.class`). This intercepts and bypasses time-consuming thread sleep delays, allowing unit tests to execute in milliseconds rather than seconds.

### Code Examples & References
* **Static Mocking & Unit Tests**:
  * See [NaukriJobBotTest.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/test/java/com/jobbot/automation/NaukriJobBotTest.java)

---

## 2. Profile-Based Integration Testing Configs
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **Test Configurations (`@TestConfiguration`)**: Overriding active production bean definitions with mock behavior specifically for test environments.
  * **Active Profiles Isolation (`@Profile("test")`)**: Disabling real scraping automation during test runs by activating mock services on the "test" profile. This prevents headless browser popups and external network calls from running during CI/CD checks.
  * **Diagnostic Debugging of Test Failures**: Analyzing stack traces to identify environmental dependencies. E.g. Diagnosing that the `contextLoads` integration test fails in clean pipelines due to a database dialect mismatch (`SET FOREIGN_KEY_CHECKS = 1` in `schema.sql` throwing a syntax error in H2).

### Code Examples & References
* **Test Profiles Configuration**:
  * See [TestConfig.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/test/java/com/Linked_jobs_Spring_boot_Automation_Services/TestConfig.java)
* **Context Test Class**:
  * See [LinkedJobsSpringBootAutomationServicesApplicationTests.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/test/java/com/Linked_jobs_Spring_boot_Automation_Services/LinkedJobsSpringBootAutomationServicesApplicationTests.java)
