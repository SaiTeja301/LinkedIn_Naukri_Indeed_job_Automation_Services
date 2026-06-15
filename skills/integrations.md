# Integration & Automation Skills

This document catalogs integration capabilities with third-party APIs (Google Gemini) and browser-automation frameworks (Selenium, Jsoup).

---

## 1. Selenium Browser Automation & Scraping
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **Anti-Bot Fingerprint Mitigation**: Adjusting Chrome execution settings (`--disable-blink-features=AutomationControlled`, `excludeSwitches=enable-automation`) to bypass security scrapers on job boards.
  * **Synchronization Utility Patterns**: Replacing brittle sleep-delays with target element visibility checking (`WebDriverWait`) and DOM document ready state polling.
  * **DOM Parsing & Fallback Extraction**: Parsing page content using Jsoup and extracting fields using XPath/CSS queries. Extracting schema.org JSON-LD scripts when standard DOM selectors fail due to code updates.

### Code Examples & References
* **Anti-Fingerprint Chrome Configuration**:
  * See [SeleniumConfig.java:L36-43](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/config/SeleniumConfig.java#L36-L43)
* **DOM Page Load Checking**:
  * See [WaitUtils.java:L88-97](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/WaitUtils.java#L88-L97)
* **DOM Selector & Fallback Parser**:
  * See [NaukriJobBot.java:L370-386](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/NaukriJobBot.java#L370-L386)

---

## 2. Gemini AI REST Integration
* **Confidence Level**: 88% (Advanced)
* **Inferred Skills**:
  * **Dynamic Context Assembly**: Transforming database structures (user profiles, active job listings, resume data) into a cohesive context string to feed to the LLM.
  * **Structured JSON Payload Construction**: Constructing nested prompt maps conforming to Google's generative models API schema.
  * **Asynchronous Client Integration**: Posting parameters and reading deserialized responses reactively via Spring WebFlux `WebClient`.

### Code Examples & References
* **Gemini Prompt Building**:
  * See [AiAgentServiceImpl.java:L48-107](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/service/impl/AiAgentServiceImpl.java#L48-L107)
* **REST Execution**:
  * See [AiAgentServiceImpl.java:L109-123](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/service/impl/AiAgentServiceImpl.java#L109-L123)
