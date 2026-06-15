# Database Architecture & Modeling Skills

This document details the database schema design, Object-Relational Mapping (ORM) practices, and database engineering capabilities demonstrated in the codebase.

---

## 1. Relational Database Design & Data Modeling
* **Confidence Level**: 85% (Advanced)
* **Inferred Skills**:
  * **Relational Schema Definition**: Designing transactional schemas using relational modeling rules, foreign key constraints, and cascade delete operations.
  * **Cascade Strategies (`ON DELETE CASCADE`)**: Structuring dependencies so child objects (`job_applications`, `resumes`) are automatically cleaned up when parent records (`jobs`, `users`) are deleted.
  * **Enum-Type SQL Serialization**: Persisting state values as strings using VARCHAR columns to guarantee readability and compatibility across schema updates (e.g. `ApplicationStatus` enum).
  * **Optimistic Locking**: Utilizing hibernate version tracking (`@Version`) to detect and reject concurrent write conflicts on entity modifications.

### Code Examples & References
* **Relational Schema Definition**:
  * See [schema.sql](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/resources/schema.sql)
* **Optimistic Locking (`@Version`)**:
  * See [ResumeEntity.java:L25-26](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/entity/ResumeEntity.java#L25-L26)
* **JPA Enum Mapping**:
  * See [JobApplicationEntity.java:L20-22](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/entity/JobApplicationEntity.java#L20-L22)

---

## 2. Spring Data JPA & Hibernate Integration
* **Confidence Level**: 90% (Advanced)
* **Inferred Skills**:
  * **ORM Mapping**: Mapping domain models to MySQL databases using Jakarta Persistence annotations.
  * **Repository Abstraction (`JpaRepository`)**: Providing standard CRUD operations without writing boilerplate JDBC code.
  * **Dialect and SQL Formatting**: Tuning Hibernate output for debugging and optimization via logging definitions and MySQL dialect rules.

### Code Examples & References
* **MySQL Configuration & Dialect**:
  * See [application.properties:L9-14](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/resources/application.properties#L9-L14)
* **Spring Data Repositories**:
  * See [UserRepository.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/repository/UserRepository.java)
  * See [JobRepository.java](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/java/com/jobbot/repository/JobRepository.java)

---

## 3. Database Testing Isolation
* **Confidence Level**: 95% (Expert)
* **Inferred Skills**:
  * **H2 In-Memory Scope**: Using H2 relational engines during test phases to isolate real schema changes and prevent side effects in dev/prod schemas.
  * **Dialect Compatibility Awareness**: Understanding the syntax differences between production engine (MySQL) and test engine (H2). E.g. H2 rejects MySQL-specific syntax such as `SET FOREIGN_KEY_CHECKS = 1` in `schema.sql` unless configured in MySQL compatibility mode (`jdbc:h2:mem:testdb;MODE=MySQL`).
* **Files**:
  * [pom.xml:L83-87](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/pom.xml#L83-L87)
  * [schema.sql:L8](file:///e:/AngularProjects/Job_Automation_with_Spring_boot/Linked_and_Naukri%20_jobs_Spring_boot_Automation_Services/src/main/resources/schema.sql#L8)

