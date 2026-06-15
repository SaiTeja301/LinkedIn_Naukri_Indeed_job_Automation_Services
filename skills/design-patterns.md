# Software Design Patterns Skills

This document details the software design patterns and coding conventions implemented in the codebase.

---

## 1. Architectural & Creational Patterns
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **Dependency Injection (DI)**: Inversion of Control achieved through Spring Boot. Constructor injection is used across controllers and services, which facilitates mock-based unit testing.
  * **Singleton Pattern**: Managed beans default to Spring's singleton scope.
  * **Lazy Initialization / Lazy Loading**: Delaying instantiation of memory/process-heavy dependencies (specifically `WebDriver`) until a scraper session is explicitly requested.

### Code Examples & References
* **Decoupled WebDriver Lazy Injection**:
  * See [NaukriJobBot.java:L76-78](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/NaukriJobBot.java#L76-L78)
  * See [SeleniumConfig.java:L24-25](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/config/SeleniumConfig.java#L24-L25)

---

## 2. Structural Patterns
* **Confidence Level**: 90% (Advanced)
* **Inferred Skills**:
  * **Data Transfer Object (DTO) Pattern**: Decoupling the database entity structures (`UserEntity`, `JobEntity`) from REST API request/response structures (`UserDto`, `JobDto`, `ScrapeRequest`). This isolates database changes from API contracts.
  * **Data Mapper Pattern**: Utilizing third-party mapper tools (`ModelMapper`) and static conversion utility classes (`EntityMapper.java`) to map DTOs to entities and vice versa.
  * **Configuration Pattern**: Encapsulating bean factory logic in dedicated configuration classes (`GeminiConfig`, `SeleniumConfig`, `ModelMapperConfig`).

### Code Examples & References
* **DTO Models**:
  * See [JobDto.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/dto/JobDto.java)
* **Static Entity Conversion**:
  * See [EntityMapper.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/mapper/EntityMapper.java)
* **ModelMapper Configuration**:
  * See [ModelMapperConfig.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/mapper/ModelMapperConfig.java)

---

## 3. Behavioral & Utility Patterns
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **Static Utility Pattern**: Implementing non-instantiable classes with private constructors (`WaitUtils`, `RateLimiter`, `ScreenshotUtils`) to group stateless helper operations.
  * **Throttling / Rate-Limiter Pattern**: Wrapping raw Thread sleep requests in descriptive functional ranges (micro, human, long, login) to mimic human interactions and limit automation frequency.

### Code Examples & References
* **Utility Private Constructor**:
  * See [WaitUtils.java:L24-26](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/WaitUtils.java#L24-L26)
* **Throttling Delay Wrapper**:
  * See [RateLimiter.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/RateLimiter.java)
