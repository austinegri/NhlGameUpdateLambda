package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = ScoreTotal.ScoreTotalBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoreTotal {
    private final int away;
    private final int home;
}
