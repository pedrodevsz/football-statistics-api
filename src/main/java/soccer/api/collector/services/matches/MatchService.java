package soccer.api.collector.services.matches;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import soccer.api.collector.dtos.matches.MatchDTO;
import soccer.api.collector.entities.matches.Match;
import soccer.api.collector.repositories.matches.MatchRepository;

@Service
@Transactional(readOnly = true)
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public Optional<MatchDTO> getMatchById(Long id) {
        return matchRepository.findById(id)
                .map(this::mapToDto);
    }

    public List<MatchDTO> getMatchesByTeamId(Long teamId) {
        return matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId).stream()
                .map(this::mapToDto)
                .toList();
    }

    private MatchDTO mapToDto(Match match) {
        String homeName = match.getHomeTeam() != null ? match.getHomeTeam().getName() : null;
        String awayName = match.getAwayTeam() != null ? match.getAwayTeam().getName() : null;

        return MatchDTO.builder()
                .id(match.getId())
                .date(match.getDate())
                .homeTeamName(homeName)
                .awayTeamName(awayName)
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .build();
    }
}
