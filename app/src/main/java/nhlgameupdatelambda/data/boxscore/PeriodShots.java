package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = PeriodShots.PeriodShotsBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeriodShots {
    private final int period;
    private final PeriodDescriptor periodDescriptor;
    private final int away;
    private final int home;
}
