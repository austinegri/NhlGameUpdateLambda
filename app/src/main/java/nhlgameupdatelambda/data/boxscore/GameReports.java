package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = GameReports.GameReportsBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameReports {
    private final String gameSummary;
    private final String eventSummary;
    private final String playByPlay;
    private final String faceoffSummary;
    private final String faceoffComparison;
    private final String rosters;
    private final String shotSummary;
    private final String shiftChart;
    private final String toiAway;
    private final String toiHome;
}
