package soccer.api.collector.controllers.matches;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import soccer.api.collector.dtos.matches.MatchDTO;
import soccer.api.collector.services.matches.MatchService;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin("*")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<Page<MatchDTO>> getMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        return ResponseEntity.ok(matchService.getMatches(page, size, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<Page<MatchDTO>> getMatchesByTeam(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        return ResponseEntity.ok(
                matchService.getMatchesByTeamId(teamId, page, size, sort)
        );
    }
}