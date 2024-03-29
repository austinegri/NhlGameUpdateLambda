package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = Clock.ClockBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Clock {
    private final String timeRemaining;
    private final int secondsRemaining;
    private final boolean running;
    private final boolean inIntermission;
}
