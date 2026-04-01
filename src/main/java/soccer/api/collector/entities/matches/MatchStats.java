package soccer.api.collector.entities.matches;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    private Integer homeShots;
    private Integer awayShots;
    private Integer homeShotsOnTarget;
    private Integer awayShotsOnTarget;
    private Integer homeCorners;
    private Integer awayCorners;
    private Integer homeFouls;
    private Integer awayFouls;
}
