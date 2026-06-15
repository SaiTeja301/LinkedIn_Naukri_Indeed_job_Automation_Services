# Security Engineering Skills

This document details the security patterns, credential safety, and secure coding practices demonstrated in the codebase.

---

## 1. Secrets Management & Environment Isolation
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **Environment Variable Binding**: Isolating third-party API keys from source control by pulling secrets at runtime using Java's `System.getenv()` wrapper. This adheres to modern Cloud Native security standards.

### Code Examples & References
* **Environment Variables**:
  * See [GeminiConfig.java:L18](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/config/GeminiConfig.java#L18)

---

## 2. Credentials Storage
* **Confidence Level**: 80% (Advanced)
* **Inferred Skills**:
  * **Password Encrypt Storage**: The `users` table is structured with a TEXT-typed `encrypted_password` column rather than plain-text password fields.

### Code Examples & References
* **Schema Definition**:
  * See [schema.sql:L15](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/resources/schema.sql#L15)
* **Entity Definition**:
  * See [UserEntity.java:L21-22](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/entity/UserEntity.java#L21-L22)

---

## 3. Architect's Security Recommendations
* **Authentication & Authorization**: The REST endpoints currently lack authentication and authorization layers (e.g. anyone can access user profiles and trigger scraping actions). We recommend:
  * Integrating **Spring Security** starter package in `pom.xml`.
  * Implementing stateless **JWT (JSON Web Token)** authentication to secure controller endpoints.
  * Adding **Role-Based Access Control (RBAC)** to restrict user mutations to the resource owner.
