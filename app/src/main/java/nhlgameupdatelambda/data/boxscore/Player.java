package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@SuperBuilder
@DynamoDbImmutable(builder = Player.PlayerBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
    private final int playerId;
    private final int sweaterNumber;
    private final Position position;
    private final Name name;

    private final int pim;
    private final String toi;
}
