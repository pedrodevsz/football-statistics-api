package soccer.api.collector.repositories.match;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import soccer.api.collector.entities.match.Match;
import soccer.api.collector.entities.match.MatchStats;

public interface MatchStatsRepository extends JpaRepository<MatchStats, Long> {
    Optional<MatchStats> findByMatch(Match match);
}
