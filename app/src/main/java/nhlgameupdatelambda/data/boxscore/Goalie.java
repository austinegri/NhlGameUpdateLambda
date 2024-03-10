package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@SuperBuilder
@DynamoDbImmutable(builder = Goalie.GoalieBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Goalie extends Player {
    private final String evenStrengthShotsAgainst;
    private final String powerPlayShotsAgainst;
    private final String shorthandedShotsAgainst;
    private final String saveShotsAgainst;
    private final int evenStrengthGoalsAgainst;
    private final int powerPlayGoalsAgainst;
    private final int shorthandedGoalsAgainst;
    private final int goalsAgainst;
}
