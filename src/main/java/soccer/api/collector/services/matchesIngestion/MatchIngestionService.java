package soccer.api.collector.services.matchesIngestion;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import soccer.api.collector.entities.matches.Match;
import soccer.api.collector.entities.matches.MatchStats;
import soccer.api.collector.entities.teams.Team;
import soccer.api.collector.repositories.matches.MatchRepository;
import soccer.api.collector.repositories.matches.MatchStatsRepository;
import soccer.api.collector.repositories.teams.TeamRepository;
import soccer.api.collector.services.csvDonwload.CsvDownloader;
import soccer.api.collector.services.csvParser.CsvParser;
import soccer.api.collector.services.csvParser.MatchCsv;

@Service
public class MatchIngestionService {

    private final CsvDownloader downloader;
    private final CsvParser parser;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final MatchStatsRepository matchStatsRepository;

    public MatchIngestionService(
            CsvDownloader downloader,
            CsvParser parser,
            TeamRepository teamRepository,
            MatchRepository matchRepository,
            MatchStatsRepository matchStatsRepository) {
        this.downloader = downloader;
        this.parser = parser;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.matchStatsRepository = matchStatsRepository;
    }

    public void importSeason() throws Exception {

        String url = "https://www.football-data.co.uk/mmz4281/2324/E0.csv";

        InputStream stream = downloader.download(url);
        List<MatchCsv> matches = parser.parse(stream);

        for (MatchCsv csv : matches) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd/MM/yy]");
            LocalDate date = LocalDate.parse(csv.getDate(), formatter);

            Team home = findOrCreateTeam(csv.getHomeTeam());
            Team away = findOrCreateTeam(csv.getAwayTeam());

            Optional<Match> existingMatch = matchRepository
                .findByDateAndHomeTeamAndAwayTeam(date, home, away);

            if (existingMatch.isPresent()) {
                createMatchStatsIfMissing(existingMatch.get(), csv);
                continue;
            }

            Integer homeGoals = safeParse(csv.getHomeGoals());
            Integer awayGoals = safeParse(csv.getAwayGoals());
            if (homeGoals == null || awayGoals == null) {
                continue;
            }

            Match match = new Match();
            match.setDate(date);
            match.setHomeTeam(home);
            match.setAwayTeam(away);
            match.setHomeScore(homeGoals);
            match.setAwayScore(awayGoals);

            Match savedMatch = matchRepository.save(match);
            createMatchStatsIfMissing(savedMatch, csv);
        }

        System.out.println("Importação finalizada!");
    }

    private Team findOrCreateTeam(String name) {
        return teamRepository.findByName(name)
                .orElseGet(() -> {
                    Team t = new Team();
                    t.setName(name);
                    return teamRepository.save(t);
                });
    }

    private void createMatchStatsIfMissing(Match match, MatchCsv csv) {
        if (match == null || csv == null) {
            return;
        }

        if (matchStatsRepository.findByMatch(match).isPresent()) {
            return;
        }

        MatchStats stats = MatchStats.builder()
                .match(match)
                .homeShots(safeParse(csv.getHomeShots()))
                .awayShots(safeParse(csv.getAwayShots()))
                .homeShotsOnTarget(safeParse(csv.getHomeShotsOnTarget()))
                .awayShotsOnTarget(safeParse(csv.getAwayShotsOnTarget()))
                .homeCorners(safeParse(csv.getHomeCorners()))
                .awayCorners(safeParse(csv.getAwayCorners()))
                .homeFouls(safeParse(csv.getHomeFouls()))
                .awayFouls(safeParse(csv.getAwayFouls()))
                .build();

        matchStatsRepository.save(stats);
    }

    private static Integer safeParse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
