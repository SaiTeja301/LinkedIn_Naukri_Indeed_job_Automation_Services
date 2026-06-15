# Backend Engineering Skills

This document details the backend engineering skills and technologies demonstrated in this codebase, along with confidence levels and references to implementation files.

---

## 1. Java 17 Core & Advanced Features
* **Confidence Level**: 92% (Expert)
* **Inferred Skills**:
  * **Functional Programming & Streams API**: Extensive use of Java 8+ Stream API features including `.stream()`, `.filter()`, `.map()`, `.flatMap()`, `.distinct()`, `.collect(Collectors.toList())`, and `IntStream` to process scraping urls and DTO mappings.
  * **Option Types (`Optional` & `OptionalInt`)**: Defensive programming patterns using `Optional` and `OptionalInt` to handle potentially null values gracefully during scraping and data extraction.
  * **Concurrency & Synchronization**: Use of the `synchronized` keyword on scraper instance methods to restrict concurrent access to the active `WebDriver` session.
  * **Standard Functional Interfaces**: Leveraging functional interfaces like `Supplier` in retry utilities to pass anonymous blocks of driver actions.

### Code Examples & References
* **Streams and Collections**: Mapping elements to strings and flattening streams.
  * See [LinkedInLoginBot.java:L86-102](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/LinkedInLoginBot.java#L86-L102)
* **Functional Interfaces**: `Supplier` used in Selenium stale-element retry utility.
  * See [WaitUtils.java:L108-122](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/WaitUtils.java#L108-L122)
* **Method Synchronization**:
  * See [LinkedInLoginBot.java:L58-59](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/LinkedInLoginBot.java#L58-L59) and [LinkedInLoginBot.java:L225](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/LinkedInLoginBot.java#L225)

---

## 2. Spring Boot 3.2.2 Ecosystem
* **Confidence Level**: 98% (Expert)
* **Inferred Skills**:
  * **Spring IoC & Dependency Injection**: Constructor injection is strictly followed (best practice for testability and lifecycle control). Autowired controllers and services interact via interfaces.
  * **Lazy Bean Initialization**: Using `@Lazy` to delay `WebDriver` chrome-driver instantiation until a scraping request is triggered. Prevents browser window popups on application startup.
  * **Configuration Property Mapping**: Injecting config properties from `application.properties` with fallback values using `@Value`.
  * **Global Exception Handling**: Mapping application errors to REST responses using `@RestControllerAdvice` and `@ExceptionHandler`.

### Code Examples & References
* **Constructor Injection & `@Lazy`**:
  * See [NaukriJobBot.java:L76-78](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/NaukriJobBot.java#L76-L78)
* **Value Annotation Injection**:
  * See [SeleniumConfig.java:L20-21](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/config/SeleniumConfig.java#L20-L21)
* **Global Exception Handler**:
  * See [GlobalExceptionHandler.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/exceptions/GlobalExceptionHandler.java)

---

## 3. Spring WebFlux & Reactive Integration
* **Confidence Level**: 85% (Advanced)
* **Inferred Skills**:
  * **Non-Blocking HTTP Client**: Constructing HTTP payloads and header keys dynamically using `WebClient` builder configurations.
  * **Reactive Return Types**: Controllers and Services returning `Mono` publishers for client requests.
  * **Hybrid Reactive-Blocking Integration**: Safely returning reactive `Mono` elements while querying blocking relational databases (e.g. `UserRepository.findById`), showing familiarity with transition trade-offs.

### Code Examples & References
* **WebClient Builder & Config**:
  * See [GeminiConfig.java:L16-24](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/config/GeminiConfig.java#L16-L24)
* **Reactivity in Services**:
  * See [AiAgentServiceImpl.java:L109-123](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/service/impl/AiAgentServiceImpl.java#L109-L123)

---

## 4. Mail Service Integration
* **Confidence Level**: 95% (Familiar / Implemented Starter)
* **Inferred Skills**:
  * **Spring Starter Mail**: Setup of `spring-boot-starter-mail` in the Maven dependencies for SMTP mail generation.
* **Files**:
  * [pom.xml](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/pom.xml#L39-L42)
