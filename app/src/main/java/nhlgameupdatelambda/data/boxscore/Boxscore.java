package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = Boxscore.BoxscoreBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Boxscore {
    private final Linescore linescore;
    private final List<PeriodShots> shotsByPeriod;
    private final GameReports gameReports;
    private final PlayerByGameStats playerByGameStats;
    private final GameInfo gameInfo;
}
