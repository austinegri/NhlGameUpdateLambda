package nhlgameupdatelambda.data.playbyplay.playdetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = PlayDetail.PlayDetailBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayDetail {
    // Common
    private final Integer eventOwnerTeamId;
    private final Integer xCoord;
    private final Integer yCoord;
    private final ZoneCode zoneCode;

    // Shot
    private final Integer blockingPlayerId; // blocked-shot
    private final Integer scoringPlayerId; // goal
    private final Integer scoringPlayerTotal; // goal
    private final Integer assist1PlayerId; // goal
    private final Integer assist1PlayerTotal; // goal
    private final Integer assist2PlayerId; // goal
    private final Integer assist2PlayerTotal; // goal
    private final Integer awayScore; // goal
    private final Integer homeScore; // goal
    private final String shotType;
    private final Integer shootingPlayerId; // shot
    private final Integer goalieInNetId; // shot
    private final Integer awaySOG; // shot-on-goal
    private final Integer homeSOG; // shot-on-goal

    // Stoppage, Missed-Shot
    private final String reason;
    private final String secondaryReason;

    // Faceoff
    private final Integer losingPlayerId;
    private final Integer winningPlayerId;

    // Giveaway, Takeaway
    private final Integer playerId;

    // Hit
    private final Integer hittingPlayerId;
    private final Integer hitteePlayerId;

    // Penalty
    private final String typeCode; // ToDo convert to Enum
    private final String descKey; // ToDo convert to Enum
    private final Integer duration;
    private final Integer committedByPlayerId;
    private final Integer drawnByPlayerId;
}
