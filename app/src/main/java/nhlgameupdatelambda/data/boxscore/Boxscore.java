package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Boxscore {
    private final Linescore linescore;
    private final Object shotsByPeriod;
    private final GameReports gameReports;
    private final PlayerByGameStats playerByGameStats;
    private final Object gameInfo;
}
