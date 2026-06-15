# Engineering Practices Skills

This document details the engineering practices, coding guidelines, and defensive automation techniques implemented in the codebase.

---

## 1. Web Automation Robustness & Fault Tolerance
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **Fail-Safe Screenshots**: Automatically capturing and persisting PNG images of the Chrome DOM structure to a `./screenshots` folder inside catching blocks. This provides visual logging for scraping failures.
  * **Stale Element Reference Retries**: Implementing a general retry utility wrapping element actions in high-order supplier interfaces to catch and retry transient Selenium stale reference errors.
  * **Fallback Metadata Extraction**: Using Google/schema.org standard JSON-LD data extractors when standard DOM selectors fail to find keys (due to frontend updates).

### Code Examples & References
* **Failure Screenshot Capture**:
  * See [ScreenshotUtils.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/ScreenshotUtils.java)
* **Stale Element Handling**:
  * See [WaitUtils.java:L108-122](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/WaitUtils.java#L108-L122)
* **JSON-LD Fallback Extractor**:
  * See [NaukriJobBot.java:L475-496](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/NaukriJobBot.java#L475-L496)

---

## 2. Defensive Selector Strategies
* **Confidence Level**: 90% (Advanced)
* **Inferred Skills**:
  * **Class Contains Matching**: Selecting elements via XPath's `contains(@class, 'name')` function rather than absolute paths or CSS module hashes (which are highly volatile and change during target system deployments).
  * **Stable Attribute Matching**: Relying on stable HTML data attributes (such as `[data-job-id]`) for tuple extractions.

### Code Examples & References
* **Defensive Selectors**:
  * See [NaukriJobBot.java:L50-63](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/automation/NaukriJobBot.java#L50-L63)

---

## 3. Human Simulation & Throttling
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **Randomized Pause Delays**: Using `ThreadLocalRandom` to insert varying pauses within execution paths. This breaks predictable automated patterns, decreasing bot-detection rates.
  * **Action Delays Slicing**: Categorizing delays into context-based ranges (micro, human, long, login) representing typing speed, click thinking time, navigation loads, and login redirection buffers.

### Code Examples & References
* **Randomized Delay Wrapper**:
  * See [RateLimiter.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/utils/RateLimiter.java)
