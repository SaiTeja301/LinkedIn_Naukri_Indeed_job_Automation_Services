package com.Linked_jobs_Spring_boot_Automation_Services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class LinkedJobsSpringBootAutomationServicesApplicationTests {

	@Test
	void contextLoads() {
	}

}
