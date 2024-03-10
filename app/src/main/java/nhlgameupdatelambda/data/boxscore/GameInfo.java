package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = GameInfo.GameInfoBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameInfo {
    private final List<Name> referees;
    private final List<Name> linesmen;
    private final GameInfoTeam awayTeam;
    private final GameInfoTeam homeTeam;
}
