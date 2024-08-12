package nhlgameupdatelambda.data.modern.individual;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = PlayerByGameIndividualStats.PlayerByGameIndividualStatsBuilder.class)
public class PlayerByGameIndividualStats {
    private final ModernIndividualRoster awayTeam;
    private final ModernIndividualRoster homeTeam;
}
