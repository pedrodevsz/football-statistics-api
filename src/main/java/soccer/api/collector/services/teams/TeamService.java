package soccer.api.collector.services.teams;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import soccer.api.collector.dtos.teams.TeamDTO;
import soccer.api.collector.entities.teams.Team;
import soccer.api.collector.repositories.teams.TeamRepository;

@Service
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public Optional<TeamDTO> getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(this::mapToDto);
    }

    private TeamDTO mapToDto(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }
}
