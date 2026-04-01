package soccer.api.collector.components;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import soccer.api.collector.services.matchesIngestion.MatchIngestionService;

@Component
public class StartupRunner implements CommandLineRunner {

    private final MatchIngestionService service;

    public StartupRunner(MatchIngestionService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        service.importSeason();
    }
}