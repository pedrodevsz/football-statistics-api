package soccer.api.collector.repositories.match;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import soccer.api.collector.entities.match.Match;
import soccer.api.collector.entities.team.Team;

public interface MatchRepository  extends JpaRepository<Match, Long> {
    Optional<Match> findByDateAndHomeTeamAndAwayTeam(
        LocalDate date,
        Team homeTeam,
        Team awayTeam
    );
}
