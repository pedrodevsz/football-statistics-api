package soccer.api.collector.repositories.matches;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import soccer.api.collector.entities.matches.Match;
import soccer.api.collector.entities.teams.Team;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByDateAndHomeTeamAndAwayTeam(
        LocalDate date,
        Team homeTeam,
        Team awayTeam
    );

    List<Match> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);
}
