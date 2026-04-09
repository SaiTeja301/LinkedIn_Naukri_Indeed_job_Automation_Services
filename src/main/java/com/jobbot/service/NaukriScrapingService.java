package com.jobbot.service;

import com.jobbot.dto.JobDto;
import com.jobbot.exceptions.InvalideUserExeption;
import java.util.List;

/**
 * Service interface for Naukri scraping operations.
 */
public interface NaukriScrapingService {

    /**
     * Authenticates to Naukri using the credentials stored for the given user email.
     *
     * @param naukriUserEmail email address used to look up credentials from the users table
     * @throws InvalideUserExeption if the user is not found or credentials are missing
     */
    void naukriLogin(String naukriUserEmail) throws InvalideUserExeption;

    /**
     * Runs a full scrape cycle:
     * logs in → navigates to search results → collects job URLs → scrapes detail pages → saves to DB.
     *
     * @param naukriUserEmail email for credential lookup
     * @param keyword         job search keyword (e.g. "java spring boot")
     * @param location        city/location filter (nullable)
     * @param experience      years of experience filter (nullable)
     * @param wfhType         work-from-home type: 0=WFO, 1=Hybrid, 2=Remote (nullable)
     * @param pageCount       number of result pages to scrape
     * @return list of saved/updated {@link JobDto} objects
     * @throws InvalideUserExeption on authentication failure
     */
    List<JobDto> scrapeAndSaveNaukriJobs(
            String naukriUserEmail,
            String keyword,
            String location,
            Integer experience,
            Integer wfhType,
            Integer pageCount) throws InvalideUserExeption;

    /**
     * Scrapes the recommended-jobs page for the logged-in user and saves them to DB.
     *
     * @param naukriUserEmail email for credential lookup
     * @return list of saved/updated {@link JobDto} objects
     * @throws InvalideUserExeption on authentication failure
     */
    List<JobDto> scrapeAndSaveRecommendedJobs(String naukriUserEmail) throws InvalideUserExeption;
}
