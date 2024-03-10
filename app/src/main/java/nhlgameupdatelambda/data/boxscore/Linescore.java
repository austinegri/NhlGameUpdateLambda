package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = Linescore.LinescoreBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Linescore {
    private final List<PeriodScore> byPeriod;
    private final ScoreTotal totals;
}
