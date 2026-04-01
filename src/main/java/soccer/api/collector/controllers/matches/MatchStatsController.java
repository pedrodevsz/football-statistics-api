package soccer.api.collector.controllers.matches;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soccer.api.collector.dtos.matches.MatchStatsDTO;
import soccer.api.collector.services.matchesStats.MatchStatsService;

@RestController
@RequestMapping("/api/stats")
public class MatchStatsController {

    private final MatchStatsService matchStatsService;

    public MatchStatsController(MatchStatsService matchStatsService) {
        this.matchStatsService = matchStatsService;
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<MatchStatsDTO> getMatchStats(@PathVariable Long matchId) {
        return matchStatsService.getStatsByMatchId(matchId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
