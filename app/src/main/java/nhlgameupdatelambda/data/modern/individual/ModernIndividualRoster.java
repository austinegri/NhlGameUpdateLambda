package nhlgameupdatelambda.data.modern.individual;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.Map;

@Data
@Builder
@DynamoDbImmutable(builder = ModernIndividualRoster.ModernIndividualRosterBuilder.class)
public class ModernIndividualRoster {
    private final Map<Integer, ModernIndividualSkater> forwards;
    private final Map<Integer, ModernIndividualSkater> defense;
//    private final List<Goalie> goalies; // ToDo
}
