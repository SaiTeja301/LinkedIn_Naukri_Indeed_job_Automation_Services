# Cloud & DevOps Skills

This document evaluates the Cloud & DevOps capabilities associated with this project.

---

## 1. Cloud & DevOps Summary
* **Confidence Level**: 0% (None detected directly in this repository)
* **Direct Evidence**:
  * No `Dockerfile`, `docker-compose.yml`, Kubernetes YAML manifests (`deployment.yaml`), Terraform files (`.tf`), or CI/CD scripts (such as GitHub Actions in `.github/workflows` or a `Jenkinsfile`) exist in this repository.

---

## 2. Architect's DevOps Recommendations
To move this project towards a cloud-native model, a Cloud & DevOps engineer should implement the following structures:

### A. Multi-Stage Dockerfile (incorporating Chrome for Selenium)
Because the application relies on Selenium to automate Chrome, the runner Docker image must package Chrome and ChromeDriver. Below is a recommended multi-stage `Dockerfile` recipe:

```dockerfile
# Stage 1: Build the Spring Boot application
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Create a secure container with Chrome & ChromeDriver
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Install Chrome and dependencies
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ca-certificates \
    apt-transport-https \
    --no-install-recommends && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list' && \
    apt-get update && \
    apt-get install -y google-chrome-stable --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Copy packaged jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run Selenium in headless mode inside the container
ENV SELENIUM_HEADLESS=true
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### B. CI/CD Pipeline Design (GitHub Actions)
A standard integration pipeline (`.github/workflows/ci.yml`) should be defined to compile code, run tests, build the Docker image, and upload the artifact to a registry (like Docker Hub or AWS ECR):

```yaml
name: Java CI/CD with Maven

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: 'maven'
    
    - name: Run Unit Tests (H2 isolated)
      run: mvn test -Ptest
      
    - name: Build Application Package
      run: mvn package -DskipTests
```
