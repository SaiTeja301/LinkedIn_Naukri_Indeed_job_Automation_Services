package com.jobbot.controller;

import com.jobbot.exceptions.InvalideUserExeption;
import com.jobbot.service.JobService;
import com.jobbot.service.NaukriScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for triggering job automation tasks.
 *
 * Endpoints:
 *   LinkedIn:
 *     POST /auto-jobs/apply-job/{id}
 *     POST /auto-jobs/apply-jobs-list
 *     GET  /auto-jobs/linkedin-jobs/scrape
 *     GET  /auto-jobs/linkedin-jobs/scrape-with-filters
 *
 *   Naukri:
 *     GET  /auto-jobs/naukri-jobs/scrape
 *     GET  /auto-jobs/naukri-jobs/scrape-recommended
 */
@RestController
@RequestMapping("/auto-jobs")
public class AutomationController {

    private final JobService jobService;
    private final NaukriScrapingService naukriScrapingService;

    @Autowired
    public AutomationController(JobService jobService,
                                NaukriScrapingService naukriScrapingService) {
        this.jobService = jobService;
        this.naukriScrapingService = naukriScrapingService;
    }

    // ── LinkedIn endpoints (unchanged) ──────────────────────────────────────

    @PostMapping("/apply-job/{id}")
    public String applyToJob(@PathVariable Long id) {
        return jobService.applyToJob(id);
    }

    @PostMapping("/apply-jobs-list")
    public String applyToJobs(@RequestBody List<Long> ids) {
        return jobService.applyToJobs(ids);
    }

    /**
     * Scrapes LinkedIn recommended + easy-apply jobs for the given user.
     *
     * @param linkedinUserEmailorUserName registered LinkedIn email (used to fetch password from DB)
     */
    @GetMapping("/linkedin-jobs/scrape")
    public String scrapeJobs(@RequestParam String linkedinUserEmailorUserName) throws InvalideUserExeption {
        var scrapedJobs = jobService.scrapeJobs(linkedinUserEmailorUserName);
        if (scrapedJobs.isEmpty()) {
            return "Scraping completed. No new jobs found.";
        }
        return "Scraping completed. Saved " + scrapedJobs.size() + " new jobs.";
    }

    /**
     * Scrapes LinkedIn jobs filtered by title and posted-within-N-hours.
     *
     * @param linkedinUserEmailorUserName registered LinkedIn email
     * @param Title                       job title keyword to filter by
     * @param timeHours                   only include jobs posted within this many hours
     */
    @GetMapping("/linkedin-jobs/scrape-with-filters")
    public String scrapeJobsWithFilters(
            @RequestParam String linkedinUserEmailorUserName,
            @RequestParam String Title,
            @RequestParam Integer timeHours) throws InvalideUserExeption {
        var scrapedJobs = jobService.scrapeJobs(linkedinUserEmailorUserName, Title, timeHours);
        if (scrapedJobs.isEmpty()) {
            return "Scraping completed. No new jobs found.";
        }
        return "Scraping completed. Saved " + scrapedJobs.size() + " new jobs.";
    }

    // ── Naukri endpoints (new) ──────────────────────────────────────────────

    /**
     * Scrapes Naukri jobs for a given keyword and filter combination.
     *
     * Example:
     *   GET /auto-jobs/naukri-jobs/scrape
     *       ?naukriUserEmail=user@example.com
     *       &keyword=java+spring+boot
     *       &location=hyderabad
     *       &experience=3
     *       &wfhType=1
     *       &pageCount=3
     *
     * @param naukriUserEmail email used to look up Naukri credentials from DB
     * @param keyword         search keyword (e.g. "java spring boot")
     * @param location        city filter (optional)
     * @param experience      years of experience filter (optional)
     * @param wfhType         work model: 0=WFO, 1=Hybrid, 2=Remote (optional)
     * @param pageCount       number of result pages to scrape (default: 5)
     */
    @GetMapping("/naukri-jobs/scrape")
    public String scrapeNaukriJobs(
            @RequestParam String naukriUserEmail,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) Integer wfhType,
            @RequestParam(required = false, defaultValue = "5") Integer pageCount)
            throws InvalideUserExeption {

        var savedJobs = naukriScrapingService.scrapeAndSaveNaukriJobs(
                naukriUserEmail, keyword, location, experience, wfhType, pageCount);

        if (savedJobs.isEmpty()) {
            return "Naukri scraping completed. No new jobs found.";
        }
        return "Naukri scraping completed. Saved/Updated " + savedJobs.size() + " jobs.";
    }

    /**
     * Scrapes the Naukri Recommended Jobs page for the logged-in user.
     *
     * Example:
     *   GET /auto-jobs/naukri-jobs/scrape-recommended?naukriUserEmail=user@example.com
     *
     * @param naukriUserEmail email used to look up Naukri credentials from DB
     */
    @GetMapping("/naukri-jobs/scrape-recommended")
    public String scrapeNaukriRecommendedJobs(
            @RequestParam String naukriUserEmail) throws InvalideUserExeption {

        var savedJobs = naukriScrapingService.scrapeAndSaveRecommendedJobs(naukriUserEmail);

        if (savedJobs.isEmpty()) {
            return "Naukri recommended jobs scraping completed. No new jobs found.";
        }
        return "Naukri recommended jobs scraping completed. Saved/Updated " + savedJobs.size() + " jobs.";
    }
}
