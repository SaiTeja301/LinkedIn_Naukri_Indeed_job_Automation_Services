package com.Linked_jobs_Spring_boot_Automation_Services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.Linked_jobs_Spring_boot_Automation_Services", "com.jobbot" })
@EnableJpaRepositories(basePackages = "com.jobbot.repository")
@EntityScan(basePackages = "com.jobbot.entity")
public class LinkedJobsSpringBootAutomationServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkedJobsSpringBootAutomationServicesApplication.class, args);
	}

}
