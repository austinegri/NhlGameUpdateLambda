package nhlgameupdatelambda.data.playbyplay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.common.Broadcast;
import nhlgameupdatelambda.data.common.Clock;
import nhlgameupdatelambda.data.common.GameOutcome;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.common.GameVideo;
import nhlgameupdatelambda.data.common.Name;
import nhlgameupdatelambda.data.common.PeriodDescriptor;
import nhlgameupdatelambda.data.common.Summary;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnoreNulls;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = PlayByPlay.PlayByPlayBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayByPlay {
    @Getter(onMethod_={@DynamoDbPartitionKey})
    private final Integer id;
    private final Integer season;
    private final Integer gameType;
    private final Boolean limitedScoring;
    private final String gameDate;
    private final Name venue;
    private final Name venueLocation;
    private final String startTimeUTC;
    private final String easternUTCOffset;
    private final String venueUTCOffset;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final List<Broadcast> tvBroadcasts;
    private final GameState gameState;
    private final String gameScheduleState;
    private final PeriodDescriptor periodDescriptor;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final PlayByPlayTeam awayTeam;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final PlayByPlayTeam homeTeam;
    private final Boolean shootoutInUse;
    private final Boolean otInUse;
    private final Clock clock;
    private final Integer displayPeriod;
    private final Integer maxPeriods;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final GameOutcome gameOutcome;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final List<Play> plays; // ToDo move to LinkedHashSet (and make convertable)
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final List<RosterSpot> rosterSpots;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final GameVideo gameVideo;
    private final Integer regPeriods;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final Summary summary;
}
