package nhlgameupdatelambda.data.modern.individual;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.modern.individual.player.ModernIndividualGoalie;
import nhlgameupdatelambda.data.modern.individual.player.ModernIndividualSkater;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = ModernIndividualRoster.ModernIndividualRosterBuilder.class)
public class ModernIndividualRoster {
    private final List<ModernIndividualSkater> forwards;
    private final List<ModernIndividualSkater> defense;
    private final List<ModernIndividualGoalie> goalies;
}
