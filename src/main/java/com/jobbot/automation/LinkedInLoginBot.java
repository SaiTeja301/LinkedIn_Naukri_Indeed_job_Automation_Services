package com.jobbot.automation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Configuration;

import org.springframework.stereotype.Component;

import com.jobbot.constants.JobConstants;
import com.jobbot.dto.JobDto;

@Component
@Configuration
// Exclude from test profile as it requires WebDriver bean
public class LinkedInLoginBot {

    private WebDriver driver;

    public LinkedInLoginBot(@org.springframework.context.annotation.Lazy WebDriver driver) {
        this.driver = driver;
    }

    public void LinkedInlogin(String email, String password) throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(JobConstants.JOB_PORTAL_LINKEDIN.getConstantValue());
        Thread.sleep((long) ((Math.random() * 19) + 2000));

        if (driver.findElements(By.xpath("//input[@id='username']")).size() > 0) {
            driver.findElement(By.xpath("//input[@id='username']")).sendKeys(email);
            Thread.sleep((long) ((Math.random() * 18) + 2000));
        }

        if (driver.findElements(By.xpath("//input[@id='password']")).size() > 0) {
            driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
            Thread.sleep((long) ((Math.random() * 17) + 2000));
        }

        if (driver.findElements(By.xpath("//button[@aria-label='Sign in']")).size() > 0) {
            driver.findElement(By.xpath("//button[@aria-label='Sign in']")).click();
            Thread.sleep((long) ((Math.random() * 11) + 2000));
        }

        Thread.sleep((long) ((Math.random() * 17) + 15000));
    }

    public synchronized java.util.List<com.jobbot.dto.JobDto> scrapeJobs() {
        java.util.List<com.jobbot.dto.JobDto> scrapedJobs = new java.util.ArrayList<>();
        try {
            // Clicking on jobs in main page

            if (driver.findElements(By.xpath("//span[@title='Jobs']")).size() > 0) {
                driver.findElement(By.xpath("//span[@title='Jobs']")).click();
                Thread.sleep((long) ((Math.random() * 19) + 5000));
            } else {
                driver.navigate().to(JobConstants.JOBS_LINKEDURL.getConstantValue());
                Thread.sleep((long) ((Math.random() * 19) + 5000));
            }

            String pageSource = driver.getPageSource();
            org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(pageSource);
            org.jsoup.select.Elements jobCountElement = doc
                    .selectXpath("//span[contains(., 'Show all')]/parent::span/parent::a");

            java.util.List<String> easyApplyjobLinks = jobCountElement.stream()
                    .map(ele -> ele.attr("href"))
                    .collect(java.util.stream.Collectors.toList());

            System.out.println("Easy Apply Job Links: " + easyApplyjobLinks + " Count: " + easyApplyjobLinks.size());

            java.util.List<String> jobUrls = new java.util.ArrayList<>();

            // implemented the pagenation location and storeing all links in this list
            // easyApplyjobLinks
            java.util.List<String> derivedJobLinks = easyApplyjobLinks.stream().filter(link -> link.contains(
                    "https://www.linkedin.com/jobs/collections/recommended/?discover=recommended&discoveryOrigin=JOBS_HOME_JYMBII")
                    || link.contains(
                            "https://www.linkedin.com/jobs/collections/easy-apply/?discover=true&discoveryOrigin=JOBS_HOME_EXPANDED_JOB_COLLECTIONS&subscriptionOrigin=JOBS_HOME"))
                    .flatMap(link -> {
                        if (link.contains(
                                "https://www.linkedin.com/jobs/collections/recommended/?discover=recommended&discoveryOrigin=JOBS_HOME_JYMBII")) {
                            return java.util.stream.IntStream.rangeClosed(0, 5).mapToObj( // Reduced range for demo;
                                                                                          // original was 11
                                    i -> "https://www.linkedin.com/jobs/collections/recommended/?discover=recommended&discoveryOrigin=JOBS_HOME_JYMBII&start="
                                            + (i * 24));
                        } else {
                            return java.util.stream.IntStream.rangeClosed(0, 5).mapToObj( // Reduced range for demo
                                    i -> "https://www.linkedin.com/jobs/collections/easy-apply/?discover=true&discoveryOrigin=JOBS_HOME_EXPANDED_JOB_COLLECTIONS&start="
                                            + (i * 25) + "&subscriptionOrigin=JOBS_HOME");
                        }
                    }).collect(java.util.stream.Collectors.toList());

            easyApplyjobLinks.addAll(derivedJobLinks);

            // getting or Scraping all job details
            easyApplyjobLinks.stream()
                    .distinct()
                    .forEach(link -> {
                        try {
                            driver.navigate().to(link);
                            Thread.sleep((long) ((Math.random() * 17) + 5000)); // Reduced delay
                            String jobPageSource = driver.getPageSource();
                            org.jsoup.nodes.Document jobDoc = org.jsoup.Jsoup.parse(jobPageSource);
                            Thread.sleep((long) ((Math.random() * 17) + 2000));
                            org.jsoup.select.Elements Jobprofiles = jobDoc.select(".ember-view");
                            java.util.List<String> extractedJobLinks = Jobprofiles.stream()
                                    .map(joblink -> joblink.select("a").attr("href"))
                                    .filter(joblink -> joblink.contains("/jobs/view/"))
                                    .map(joblink -> "https://www.linkedin.com" + joblink)
                                    .distinct()
                                    .collect(java.util.stream.Collectors.toList());

                            jobUrls.addAll(extractedJobLinks);
                        } catch (Exception e) {
                            System.out.println("Failed to process link: " + link + " due to: " + e.getMessage());
                        }
                    });

            scrapedJobs = scrapeingTheJobUrl(jobUrls);

        } catch (Exception e) {
            System.out.println("Failed Due to Cause of :" + e.getMessage());
            quitwebdriver();
        } finally {
            quitwebdriver();
        }
        return scrapedJobs;
    }

    public synchronized List<JobDto> scrapeLatestJobsByTitleandTime(String Title, Integer Hours) {
        List<JobDto> scrapedJobs = new ArrayList<>();
        List<String> jobUrls = new ArrayList<>();
        try {
            long hoursInSecounds = (Hours * 60) * 60;
            String url = "https://www.linkedin.com/jobs/search-results/?f_TPR=r" + hoursInSecounds + "&keywords="
                    + Title + "%20Developer&origin=JOB_COLLECTION_PAGE_SEARCH_BUTTON";
            driver.navigate().to(url);
            Thread.sleep((long) ((Math.random() * 17) + 2000));
            if (driver.findElements(By.cssSelector(".jobs-search-pagination__pages li button span")).size() > 0) {
                List<String> pagenationJobLinks = new ArrayList<>();
                List<WebElement> pageButtons = driver
                        .findElements(By.cssSelector(".jobs-search-pagination__pages li button span"));
                if (!pageButtons.isEmpty()) {
                    OptionalInt maxPage = pageButtons.stream()
                            .map(WebElement::getText)
                            .map(String::trim)
                            .filter(text -> !text.isEmpty())
                            .filter(text -> text.matches("\\d+"))
                            .mapToInt(Integer::parseInt)
                            .max();
                    if (!maxPage.isEmpty()) {
                        int MaxpageInt = maxPage.getAsInt();
                        for (int i = 0; i < MaxpageInt; i++) {
                            String jobPageUrl = url + "&start=" + (i * 25);
                            pagenationJobLinks.add(jobPageUrl);
                        }
                        pagenationJobLinks.stream()
                                .distinct()
                                .forEach(JobPagelink -> {
                                    try {
                                        System.out.println("Navigating to Job URL: " + JobPagelink);
                                        driver.navigate().to(JobPagelink);
                                        Thread.sleep((long) ((Math.random() * 17) + 3000));
                                        String jobDetailPageSource = driver.getPageSource();
                                        Document jobDetailDoc = Jsoup.parse(jobDetailPageSource);
                                        Elements jobElements = jobDetailDoc
                                                .select(".ember-view .job-card-job-posting-card-wrapper a");
                                        List<String> jobLinks = jobElements.stream()
                                                .map(ele -> ele.attr("href"))
                                                .filter(link -> link.contains(
                                                        "https://www.linkedin.com/jobs/search-results/?currentJobId"))
                                                .map(link -> StringUtils.substringBetween(link,
                                                        "https://www.linkedin.com/jobs/search-results/?currentJobId=",
                                                        "&keywords"))
                                                .map(link -> "https://www.linkedin.com/jobs/view/" + link + "/")
                                                .distinct()
                                                .collect(java.util.stream.Collectors.toList());
                                        jobUrls.addAll(jobLinks);
                                    } catch (Exception e) {
                                        System.out.println("Failed to process link: " + JobPagelink + " due to: "
                                                + e.getMessage());
                                    }
                                });
                        scrapedJobs = scrapeingTheJobUrl(jobUrls);
                    }
                }
            } else {
                driver.navigate().to(url);
                Thread.sleep((long) ((Math.random() * 17) + 3000));
                String jobDetailPageSource = driver.getPageSource();
                Document jobDetailDoc = Jsoup.parse(jobDetailPageSource);
                Elements jobElements = jobDetailDoc.select(".ember-view .job-card-job-posting-card-wrapper a");
                List<String> jobLinks = jobElements.stream()
                        .map(ele -> ele.attr("href"))
                        .filter(link -> link.contains("https://www.linkedin.com/jobs/search-results/?currentJobId"))
                        .map(link -> StringUtils.substringBetween(link,
                                "https://www.linkedin.com/jobs/search-results/?currentJobId=", "&keywords"))
                        .map(link -> "https://www.linkedin.com/jobs/view/" + link + "/")
                        .distinct()
                        .collect(java.util.stream.Collectors.toList());
                jobUrls.addAll(jobLinks);

                scrapedJobs = scrapeingTheJobUrl(jobUrls);
            }
        } catch (Exception e) {
            System.out.println("Failed Due to Cause of :" + e.getMessage());
            quitwebdriver();
        } finally {
            quitwebdriver();
        }
        return scrapedJobs;
    }

    public List<JobDto> scrapeingTheJobUrl(List<String> jobUrls) {

        List<JobDto> scrapedJobs = new ArrayList<>();
        jobUrls.stream()
                .distinct()
                .forEach(url -> {
                    try {
                        System.out.println("Navigating to Job URL: " + url);
                        driver.navigate().to(url);
                        Thread.sleep((long) ((Math.random() * 17) + 3000));
                        String jobDetailPageSource = driver.getPageSource();
                        org.jsoup.nodes.Document jobDetailDoc = org.jsoup.Jsoup.parse(jobDetailPageSource);

                        // Extract Key Details
                        String description = jobDetailDoc
                                .selectXpath("//*[@id='workspace']//div[2]/div[3]//div/div/div/div/div/p").text();
                        String title = jobDetailDoc
                                .selectXpath("//*[@id='workspace']//div[div/a]/following-sibling::div[1]/div/p").text(); // Rough
                                                                                                                         // assumption
                        String company = jobDetailDoc.selectXpath(
                                "(//*[@id='workspace']//div[div/a])[1]")
                                .text();

                        String location = jobDetailDoc.selectXpath(
                                "(//*[@id='workspace']//div/div/div/div[1]/div/div/div[2]/div/div[1]/p/span[1])[1]")
                                .text();

                        Elements parent = jobDetailDoc.selectXpath("//*[@id='workspace']//p");
                        String jobPosted = parent.select("span:nth-of-type(4)").text();

                        String applyCountStatus = jobDetailDoc.selectXpath("//*[@id='workspace']//p/span[7]").text();
                        url = StringUtils.substringBefore(url, "?");
                        System.out.println("Job Url : " + url);
                        // System.out.println("Job Details: " + description);

                        // Create JobDto
                        com.jobbot.dto.JobDto job = new com.jobbot.dto.JobDto();
                        job.setJobUrl(url);
                        job.setDescription(description);
                        job.setTitle(title.isEmpty() ? "Unknown Title" : title);
                        job.setCompany(company.isEmpty() ? "Unknown Company" : company);
                        job.setLocation(location.isEmpty() ? "Unknown Location" : location);
                        job.setJob_posted(jobPosted.isEmpty() ? "Not Specified" : jobPosted);
                        job.setJob_applyed_count_status(
                                applyCountStatus.isEmpty() ? "Not Specified" : applyCountStatus);

                        scrapedJobs.add(job);
                    } catch (Exception e) {
                        System.out.println("Failed to navigate to URL: " + url + " due to: " + e.getMessage());
                    }
                });
        return scrapedJobs;
    }

    public void quitwebdriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
