package nhlgameupdatelambda.data.modern.individual;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.boxscore.PlayerByGameStats;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = PlayerByGameStats.PlayerByGameStatsBuilder.class)
public class PlayerByGameIndividualStats {
    private final ModernIndividualRoster awayTeam;
    private final ModernIndividualRoster homeTeam;
}
