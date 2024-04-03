package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.common.*;
import nhlgameupdatelambda.data.common.GameOutcome;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.common.GameVideo;
import nhlgameupdatelambda.data.common.PeriodDescriptor;
import nhlgameupdatelambda.data.common.Summary;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = BoxscoreResponse.BoxscoreResponseBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoxscoreResponse {
    @Getter(onMethod_={@DynamoDbPartitionKey})
    private final Integer id;
    private final Integer season;
    private final Integer gameType;
    private final String gameDate;
    private final Name venue;
    private final String startTimeUTC;
    private final String easternUTCOffset;
    private final List<Broadcast> tvBroadcasts;
    private final GameState gameState;
    private final String gameScheduleState;
    private final Integer period;
    private final PeriodDescriptor periodDescriptor;
    private final BoxscoreTeam awayTeam;
    private final BoxscoreTeam homeTeam;
    private final Clock clock;
    private final PlayerByGameStats playerByGameStats;
    private final Summary summary;
    private final GameOutcome gameOutcome;
    private final GameVideo gameVideo;
}
