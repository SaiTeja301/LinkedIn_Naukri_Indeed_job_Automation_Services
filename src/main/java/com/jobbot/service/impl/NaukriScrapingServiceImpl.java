package com.jobbot.service.impl;

import com.jobbot.automation.NaukriJobBot;
import com.jobbot.dto.JobDto;
import com.jobbot.dto.UserDto;
import com.jobbot.exceptions.InvalideUserExeption;
import com.jobbot.service.JobScrapingService;
import com.jobbot.service.NaukriScrapingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Implementation of {@link NaukriScrapingService}.
 *
 * Execution flow:
 *   1. Resolve credentials from DB via UserService
 *   2. Delegate login to NaukriJobBot
 *   3. Build a search URL and delegate scraping to NaukriJobBot
 *   4. Delegate DB persistence + deduplication to JobScrapingService.SaveJobs()
 *
 * Key fixes over previous version:
 *   - Login is now called before any scraping (was missing entirely)
 *   - The built search URL is actually passed to the bot (was silently discarded before)
 *   - pageCount is forwarded to the bot (was not a parameter before)
 *   - SLF4J logging replaces all System.out.println
 */
@Service
@Transactional
public class NaukriScrapingServiceImpl implements NaukriScrapingService {

    private static final Logger log = LoggerFactory.getLogger(NaukriScrapingServiceImpl.class);

    @Value("${naukri.max.pages:5}")
    private int defaultMaxPages;

    private final NaukriJobBot naukriJobBot;
    private final JobScrapingService jobScrapingService;
    private final UserServiceImpl userService;

    public NaukriScrapingServiceImpl(
            NaukriJobBot naukriJobBot,
            JobScrapingService jobScrapingService,
            UserServiceImpl userService) {
        this.naukriJobBot = naukriJobBot;
        this.jobScrapingService = jobScrapingService;
        this.userService = userService;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // LOGIN
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Resolves Naukri credentials from the DB and logs in via the bot.
     * Mirrors the LinkedIn login pattern in {@link JobScrapingServiceImpl}.
     */
    @Override
    public void naukriLogin(String naukriUserEmail) throws InvalideUserExeption {
        log.info("Resolving Naukri credentials for: {}", naukriUserEmail);
        try {
            UserDto user = userService.getUserByEmail(naukriUserEmail);
            if (user == null) {
                throw new InvalideUserExeption("User not found with email: " + naukriUserEmail);
            }
            if (!user.getEmail().equalsIgnoreCase(naukriUserEmail)) {
                throw new InvalideUserExeption("User email mismatch: " + naukriUserEmail);
            }

            String email    = user.getEmail().trim();
            String password = user.getEncryptedPassword().trim();

            log.info("Credentials resolved — initiating Naukri login...");
            naukriJobBot.naukriLogin(email, password);
            log.info("Naukri login step complete.");

        } catch (InvalideUserExeption e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during Naukri login: {}", e.getMessage(), e);
            throw new InvalideUserExeption("Naukri login failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // KEYWORD-BASED SCRAPE
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Full scrape cycle for keyword-based job search:
     *   login → build URL → scrape N pages → deduplicate → save to DB.
     */
    @Override
    public List<JobDto> scrapeAndSaveNaukriJobs(
            String naukriUserEmail,
            String keyword,
            String location,
            Integer experience,
            Integer wfhType,
            Integer pageCount) throws InvalideUserExeption {

        // Step 1: Login
        naukriLogin(naukriUserEmail);

        // Step 2: Build the search URL (and actually use it — fixed bug from previous version)
        int pages = (pageCount != null && pageCount > 0) ? pageCount : defaultMaxPages;
        String searchUrl = buildNaukriSearchUrl(keyword, location, experience, wfhType);
        log.info("Naukri search URL: {}", searchUrl);

        try {
            // Step 3: Scrape using the URL (previously scrapeRecomededJobsByUrl() was called here,
            //         ignoring the built URL entirely — now correctly passed to the bot)
            List<JobDto> scrapedJobs = naukriJobBot.scrapeJobsBySearchUrl(searchUrl, pages);
            scrapedJobs.forEach(job -> job.setPlatform("Naukri"));
            log.info("Scraped {} jobs from Naukri.", scrapedJobs.size());

            // Step 4: Deduplicate + save (reusing the same robust logic as LinkedIn)
            List<JobDto> savedJobs = jobScrapingService.SaveJobs(scrapedJobs);
            log.info("Saved/Updated {} Naukri jobs in DB.", savedJobs.size());
            return savedJobs;

        } catch (Exception e) {
            log.error("Error during Naukri scrape-and-save: {}", e.getMessage(), e);
            throw new InvalideUserExeption("Naukri scraping failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RECOMMENDED JOBS SCRAPE
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Scrapes recommended jobs for the logged-in Naukri user and persists them.
     */
    @Override
    public List<JobDto> scrapeAndSaveRecommendedJobs(String naukriUserEmail) throws InvalideUserExeption {
        // Step 1: Login
        naukriLogin(naukriUserEmail);

        try {
            // Step 2: Scrape recommended jobs
            List<JobDto> scrapedJobs = naukriJobBot.scrapeRecommendedJobs();
            scrapedJobs.forEach(job -> job.setPlatform("Naukri"));
            log.info("Scraped {} recommended Naukri jobs.", scrapedJobs.size());

            // Step 3: Deduplicate + save
            List<JobDto> savedJobs = jobScrapingService.SaveJobs(scrapedJobs);
            log.info("Saved/Updated {} recommended Naukri jobs in DB.", savedJobs.size());
            return savedJobs;

        } catch (Exception e) {
            log.error("Error scraping recommended jobs: {}", e.getMessage(), e);
            throw new InvalideUserExeption("Recommended jobs scraping failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // URL BUILDER
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Constructs a valid Naukri search URL from the given filter parameters.
     *
     * URL structure:
     *   https://www.naukri.com/{keyword-slug}-jobs[-in-{location-slug}]
     *       ?k={encoded_keyword}[&l={encoded_location}][&experience=N][&wfhType=N]
     *       &nignbevent_src=jobsearchDeskGNB
     *
     * wfhType values: 0=WFO, 1=Hybrid, 2=Remote, 3=Hybrid (Naukri uses 3 for hybrid in some contexts)
     */
    private String buildNaukriSearchUrl(String keyword, String location,
                                        Integer experience, Integer wfhType) {
        StringBuilder path   = new StringBuilder("https://www.naukri.com/");
        StringBuilder params = new StringBuilder();

        // Path slug
        if (keyword != null && !keyword.isBlank()) {
            path.append(keyword.toLowerCase().trim().replace(" ", "-")).append("-jobs");
        } else {
            path.append("jobs");
        }

        if (location != null && !location.isBlank()) {
            path.append("-in-").append(location.toLowerCase().trim().replace(" ", "-"));
        }

        path.append("?");

        // Query params
        if (keyword != null && !keyword.isBlank()) {
            params.append("k=").append(encode(keyword)).append("&");
        }
        if (location != null && !location.isBlank()) {
            params.append("l=").append(encode(location)).append("&");
        }
        if (experience != null) {
            params.append("experience=").append(experience).append("&");
        }
        if (wfhType != null) {
            params.append("wfhType=").append(wfhType).append("&");
        }

        params.append("nignbevent_src=jobsearchDeskGNB");

        return path.append(params).toString();
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value.replace(" ", "%20");
        }
    }
}
