package com.jobbot.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JobConstantsTest {

    @Test
    void testEnumValues() {
        assertNotNull(JobConstants.JOB_PORTAL_LINKEDIN.getConstantValue());
        assertEquals("https://www.linkedin.com/login", JobConstants.JOB_PORTAL_LINKEDIN.getConstantValue());

        assertNotNull(JobConstants.JOBS_LINKEDURL.getConstantValue());
        assertEquals("https://www.linkedin.com/jobs/?", JobConstants.JOBS_LINKEDURL.getConstantValue());

        assertNotNull(JobConstants.JOB_PORTAL_NAUKRI.getConstantValue());
        assertEquals("https://www.naukri.com/nlogin/login", JobConstants.JOB_PORTAL_NAUKRI.getConstantValue());
    }
}
