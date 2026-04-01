package soccer.api.collector.repositories.matches;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import soccer.api.collector.entities.matches.Match;
import soccer.api.collector.entities.matches.MatchStats;

public interface MatchStatsRepository extends JpaRepository<MatchStats, Long> {
    Optional<MatchStats> findByMatch(Match match);
    Optional<MatchStats> findByMatchId(Long matchId);
}
