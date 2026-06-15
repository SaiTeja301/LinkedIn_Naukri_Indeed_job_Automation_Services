# Frontend Development Skills

This document catalogs frontend development skills associated with this project.

---

## 1. Frontend Status Summary
* **Confidence Level**: 0% (None detected directly in this repository)
* **Direct Evidence**:
  * No Angular, React, Vue, HTML, CSS, SCSS, or direct Javascript/TypeScript files exist within the boundaries of this repository.
  * The `/src/main/resources/static` and `/src/main/resources/templates` directories are completely empty, indicating that server-side page templates (like Thymeleaf, Freemarker, or JSP) or bundled SPA resources are not packaged in this Maven build.

---

## 2. Workspace Context & Inferences
* **Parent Path Clues**:
  * The local project is located inside a parent directory named `AngularProjects`: `e:\AngularProjects\Job_Automation_with_Spring_boot\Linked_and_Naukri _jobs_Spring_boot_Automation_Services`.
  * This strongly implies that there is a companion or sibling project written in **Angular** that acts as the user interface for this Job Automation platform, communicating with the REST endpoints defined in this service.

---

## 3. SPA Integration & REST Design Patterns
Although there is no frontend code in this repository, the backend REST controllers demonstrate clean integration design for Single Page Applications (SPAs) like Angular:
* **JSON Exchange Formats**: Request and Response payloads are strictly standard JSON DTOs, which map naturally to TypeScript classes or interfaces.
* **Component-Driven Endpoint Layout**: Controllers are sliced logically by resources (e.g. `UserController`, `JobController`, `ResumeController`, `AgentController`), which align with common frontend routing structures.
* **Multipart File Upload Handling**: The controller exposes standard file multipart inputs for PDF resume uploading, which is compatible with standard HTML5 file upload inputs.

### Code Examples & References
* **Standard REST Controllers & Mapping**:
  * See [AgentController.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/controller/AgentController.java)
  * See [UserController.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/controller/UserController.java)
* **JSON Request Payloads (DTO Pattern)**:
  * See [ScrapeRequest.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/dto/ScrapeRequest.java)
