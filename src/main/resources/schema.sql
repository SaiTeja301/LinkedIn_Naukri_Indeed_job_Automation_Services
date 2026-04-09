-- Drop tables in reverse order of dependencies to avoid foreign key constraint errors
DROP TABLE IF EXISTS resumes;
DROP TABLE IF EXISTS job_applications;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS ai_responses;
DROP TABLE IF EXISTS users;

-- 1. Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    encrypted_password TEXT,
    experience_years INT,
    preferred_roles TEXT,
    preferred_companies TEXT,
    remote BOOLEAN NOT NULL,
    hybrid BOOLEAN NOT NULL
);

-- 2. Jobs Table
CREATE TABLE jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    company VARCHAR(255),
    location TEXT,
    job_posted VARCHAR(255),
    job_applyed_count_status VARCHAR(255),
    job_url TEXT,
    platform VARCHAR(255),
    description TEXT,
    applied BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL
);

-- 3. Job Applications Table
CREATE TABLE job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    status VARCHAR(255),
    applied_at DATETIME(6),
    is_job_applied BOOLEAN NOT NULL,
    CONSTRAINT fk_job_application_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
);

-- 4. Resumes Table
CREATE TABLE resumes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT,
    version VARCHAR(255),
    version_count INT,
    uploaded_at DATETIME(6),
    CONSTRAINT fk_resume_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. AI Responses Table
CREATE TABLE ai_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prompt TEXT,
    response TEXT,
    created_at DATETIME(6)
);
