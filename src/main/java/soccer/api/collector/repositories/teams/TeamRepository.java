package soccer.api.collector.repositories.teams;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import soccer.api.collector.entities.teams.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
}
