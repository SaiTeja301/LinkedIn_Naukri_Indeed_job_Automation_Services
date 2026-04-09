package com.jobbot.constants;




public enum JobConstants {
	JOB_PORTAL_LINKEDIN("https://www.linkedin.com/login"),
	JOBS_LINKEDURL("https://www.linkedin.com/jobs/?"),
	JOB_PORTAL_NAUKRI("https://www.naukri.com/nlogin/login");
	
	private final String constantValue;
	
	private JobConstants(String constantValue) {
		this.constantValue = constantValue;
	}
	
	public String getConstantValue() {
		return constantValue;
	}	
	
}
