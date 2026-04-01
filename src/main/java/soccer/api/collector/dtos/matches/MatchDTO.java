package soccer.api.collector.dtos.matches;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDTO {
    private Long id;
    private LocalDate date;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeScore;
    private Integer awayScore;
}
