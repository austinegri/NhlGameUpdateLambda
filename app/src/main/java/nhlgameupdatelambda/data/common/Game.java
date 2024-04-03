package nhlgameupdatelambda.data.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = Game.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
    private final int id;
    private final int season;
    private final int gameType;
    private final String gameDate;
}
