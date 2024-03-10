package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.GameState;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = BoxscoreResponse.BoxscoreResponseBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxscoreResponse {
    @Getter(onMethod_={@DynamoDbPartitionKey})
    private final int id;
    private final int season;
    private final int gameType;
    private final String gameDate;
    private final Venue venue;
    private final String startTimeUTC;
    private final String easternUTCOffset;
    private final List<Broadcast> tvBroadcasts;
    private final GameState gameState;
    private final String gameScheduleState;
    private final int period;
    private final PeriodDescriptor periodDescriptor;
    private final BoxscoreTeam awayTeam;
    private final BoxscoreTeam homeTeam;
    private final Clock clock;
    private final Boxscore boxscore;
    private final GameOutcome gameOutcome;
    private final GameVideo gameVideo;
}
