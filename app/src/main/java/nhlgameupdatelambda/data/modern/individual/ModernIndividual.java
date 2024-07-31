package nhlgameupdatelambda.data.modern.individual;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@Builder
@DynamoDbImmutable(builder = ModernIndividual.ModernIndividualBuilder.class)
public class ModernIndividual {
    @Getter(onMethod_={@DynamoDbPartitionKey})
    private final Integer id;
    private final PlayerByGameIndividualStats playerByGameIndividualStats;
}
