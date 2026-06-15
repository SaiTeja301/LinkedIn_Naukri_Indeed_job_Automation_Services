# Technical Skills Inventory

This document represents a living inventory of technical skills demonstrated in this repository, analyzed from the perspective of a Principal Engineer, Software Architect, and Technical Recruiter.

---

## 1. Master Inventory Summary

| Skill Category | Skill | Proficiency Level | Confidence Score | Primary Evidence |
| :--- | :--- | :--- | :--- | :--- |
| **Backend** | Spring Boot | Expert | 98% | Parent POM, annotations, properties |
| **Backend** | Core & Advanced Java 17 | Expert | 92% | Stream API, lambda expressions, Generics |
| **Backend** | Spring WebFlux (Reactive) | Advanced | 85% | WebClient, Mono reactivity |
| **Database** | Spring Data JPA & Hibernate | Advanced | 90% | Entity mappings, repository interfaces |
| **Database** | Relational Modeling | Advanced | 85% | Foreign keys, cascade deletions, schema.sql |
| **Automation** | Selenium WebBrowser Automation| Expert | 95% | WebDriver config, automated login/actions |
| **Automation** | HTML Parsing & Web Scraping | Advanced | 90% | Jsoup DOM parsing, XPath, CSS selectors |
| **Integration**| Generative AI (Gemini LLM) | Advanced | 88% | Context prompt building, API integration |
| **Testing** | Unit & Integration Testing | Advanced | 85% | JUnit 5, Mockito static mocking |
| **Security** | Security Architecture | Advanced | 80% | Encrypted password fields, Env variables |
| **Frontend** | Angular / Web UI | Not Detected | 0% | No frontend files present in repository |
| **DevOps** | CI/CD & Orchestration | Not Detected | 0% | No Docker/Kubernetes/Pipeline files |

---

## 2. Skill Profiles, Evidence & File References

### Spring Boot
* **Proficiency**: Expert
* **Confidence**: 98%
* **Evidence**:
  * Spring Boot starter parent definition (version `3.2.2`).
  * Spring Boot dependency starters: `data-jpa`, `mail`, `webflux`, `web`, `test`.
  * `@SpringBootApplication` setup class.
  * Standard properties-driven configuration.
* **Files**:
  * [pom.xml](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/pom.xml#L6-L11)
  * [LinkedJobsSpringBootAutomationServicesApplication.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/Linked_jobs_Spring_boot_Automation_Services/LinkedJobsSpringBootAutomationServicesApplication.java#L8-L12)
  * [application.properties](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/resources/application.properties)

### Selenium WebDriver (Browser Automation)
* **Proficiency**: Expert
* **Confidence**: 95%
* **Evidence**:
  * Browser initialization with `ChromeOptions` modifications (headless configuration, anti-bot controls, window maximizing).
  * Automated login and navigation sequences for LinkedIn and Naukri.
  * Condition-based synchronizations (`WebDriverWait`) and stale element retries.
* **Files**:
  * [SeleniumConfig.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/config/SeleniumConfig.java)
  * [NaukriJobBot.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/NaukriJobBot.java)
  * [LinkedInLoginBot.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/LinkedInLoginBot.java)

### Core & Advanced Java 17
* **Proficiency**: Expert
* **Confidence**: 92%
* **Evidence**:
  * Stream mapping and filtering, lambda expressions, standard interfaces (`Supplier`, `Runnable`).
  * Use of Java 17 properties such as `java.time` APIs and pattern matching.
  * Generics in utility classes.
* **Files**:
  * [WaitUtils.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/WaitUtils.java#L108-L122)
  * [LinkedInLoginBot.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/LinkedInLoginBot.java#L76-L102)

### Spring WebFlux (Reactive Programming)
* **Proficiency**: Advanced
* **Confidence**: 85%
* **Evidence**:
  * Reactive `WebClient` configuration and integration.
  * Non-blocking REST endpoints returning `Mono<AiResponseDto>`.
* **Files**:
  * [GeminiConfig.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/config/GeminiConfig.java#L16-L24)
  * [AgentController.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/controller/AgentController.java#L22-L30)
  * [AiAgentServiceImpl.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/service/impl/AiAgentServiceImpl.java#L109-L122)

### Spring Data JPA & Hibernate
* **Proficiency**: Advanced
* **Confidence**: 90%
* **Evidence**:
  * Entity mappings (`@Entity`, `@Table`, `@Id`, `@ManyToOne`, `@JoinColumn`, `@Enumerated`).
  * Standard JPA Repositories extends `JpaRepository`.
* **Files**:
  * [UserEntity.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/entity/UserEntity.java)
  * [JobApplicationEntity.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/entity/JobApplicationEntity.java)
  * [UserRepository.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/repository/UserRepository.java)

### HTML Parsing & Web Scraping
* **Proficiency**: Advanced
* **Confidence**: 90%
* **Evidence**:
  * DOM document parsing and element selections using Jsoup (`Jsoup.parse`, CSS selectors, `selectXpath`).
  * Fallback parsing logic via JSON-LD schemas.
* **Files**:
  * [NaukriJobBot.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/NaukriJobBot.java#L286-L337)
  * [LinkedInLoginBot.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/LinkedInLoginBot.java#L225-L277)

### Generative AI (Gemini API Integration)
* **Proficiency**: Advanced
* **Confidence**: 88%
* **Evidence**:
  * LLM prompt creation, contextual payload generation combining database profiles, resumes, and scraped jobs.
  * Direct invocation of Gemini endpoint via `WebClient`.
* **Files**:
  * [AiAgentServiceImpl.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/service/impl/AiAgentServiceImpl.java#L41-L123)

### Unit & Integration Testing
* **Proficiency**: Advanced
* **Confidence**: 85%
* **Evidence**:
  * Mockito extensions, constructor and field mocks.
  * Mockito static mocking on utility classes.
  * Test-specific configuration and profiles (`@TestConfiguration`, `@Profile("test")`).
* **Files**:
  * [NaukriJobBotTest.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/test/java/com/jobbot/automation/NaukriJobBotTest.java)
  * [TestConfig.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/test/java/com/Linked_jobs_Spring_boot_Automation_Services/TestConfig.java)
