package nhlgameupdatelambda.data.playbyplay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.common.Name;
import nhlgameupdatelambda.data.common.Position;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = RosterSpot.RosterSpotBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RosterSpot {
    private final Integer teamId;
    private final Integer playerId;
    private final Name firstName;
    private final Name lastName;
    private final Integer sweaterNumber;
    private final Position positionCode;
    @JsonProperty("headshot")
    private final String headshotHtml;
}
