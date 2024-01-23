package nhlgameupdatelambda.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxscoreResponse {
    private final int id;
    private final int season;
    private final int gameType;
    private final String gameDate;
    private final Object venue;
    private final String startTimeUTC;
    private final String easternUTCOffset;
    private final List<Object> tvBroadcasts;
    private final GameState gameState;
    private final String gameScheduleState;
    private final int period;
    private final Object periodDescriptor;
    private final Object awayTeam;
    private final Object homeTeam;
    private final Object clock;
    private final Object boxscore;
    private final Object gameOutcome;
    private final Object gameVideo;
}
