package soccer.api.collector.services.matchesStats;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import soccer.api.collector.dtos.matches.MatchStatsDTO;
import soccer.api.collector.entities.matches.MatchStats;
import soccer.api.collector.repositories.matches.MatchStatsRepository;

@Service
@Transactional(readOnly = true)
public class MatchStatsService {

    private final MatchStatsRepository matchStatsRepository;

    public MatchStatsService(MatchStatsRepository matchStatsRepository) {
        this.matchStatsRepository = matchStatsRepository;
    }

    public Optional<MatchStatsDTO> getStatsByMatchId(Long matchId) {
        return matchStatsRepository.findByMatchId(matchId)
                .map(this::mapToDto);
    }

    private MatchStatsDTO mapToDto(MatchStats stats) {
        return MatchStatsDTO.builder()
                .id(stats.getId())
                .matchId(stats.getMatch() != null ? stats.getMatch().getId() : null)
                .homeShots(stats.getHomeShots())
                .awayShots(stats.getAwayShots())
                .homeShotsOnTarget(stats.getHomeShotsOnTarget())
                .awayShotsOnTarget(stats.getAwayShotsOnTarget())
                .homeCorners(stats.getHomeCorners())
                .awayCorners(stats.getAwayCorners())
                .homeFouls(stats.getHomeFouls())
                .awayFouls(stats.getAwayFouls())
                .build();
    }
}
