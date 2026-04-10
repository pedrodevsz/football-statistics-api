package soccer.api.collector.services.matches;

import java.util.Optional;

import org.springframework.data.domain.*;
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

    public Page<MatchDTO> getMatches(int page, int size, String sort) {

        Sort.Direction direction = sort.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "date"));

        return matchRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    public Optional<MatchDTO> getMatchById(Long id) {
        return matchRepository.findById(id)
                .map(this::mapToDto);
    }

    public Page<MatchDTO> getMatchesByTeamId(Long teamId, int page, int size, String sort) {

        Sort.Direction direction = sort.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "date"));

        return matchRepository
                .findByHomeTeamIdOrAwayTeamId(teamId, teamId, pageable)
                .map(this::mapToDto);
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