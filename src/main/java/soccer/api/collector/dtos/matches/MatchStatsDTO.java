package soccer.api.collector.dtos.matches;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchStatsDTO {
    private Long id;
    private Long matchId;
    private Integer homeShots;
    private Integer awayShots;
    private Integer homeShotsOnTarget;
    private Integer awayShotsOnTarget;
    private Integer homeCorners;
    private Integer awayCorners;
    private Integer homeFouls;
    private Integer awayFouls;
}
