package soccer.api.collector.components;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import soccer.api.collector.services.matchesIngestion.MatchIngestionService;

@Component
public class MatchUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MatchUpdateScheduler.class);

    private final MatchIngestionService service;
    private final Object lock = new Object();
    private volatile boolean isRunning = false;

    public MatchUpdateScheduler(MatchIngestionService service) {
        this.service = service;
    }

    @Scheduled(fixedDelayString = "${match.update.fixed-delay}", initialDelayString = "${match.update.initial-delay}")
    public void runScheduledUpdate() {
        synchronized (lock) {
            if (isRunning) {
                logger.info("Previous job still running. Skipping this execution.");
                return;
            }
            isRunning = true;
        }

        try {
            logger.info("Starting scheduled match update...");
            service.importSeason();
            logger.info("Match update finished successfully.");
        } catch (Exception ex) {
            logger.error("Error during scheduled match update", ex);
        } finally {
            synchronized (lock) {
                isRunning = false;
            }
        }
    }
}
