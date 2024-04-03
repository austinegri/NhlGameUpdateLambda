package nhlgameupdatelambda.data.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Data
@SuperBuilder
@DynamoDbImmutable(builder = Summary.SummaryBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Summary {
//    private final List<JsonNode> teamGameStats; // ToDo update to TeamGameStats
//    private final List<JsonNode> seasonSeries;
//    private final JsonNode seasonSeriesWins;
    private final Linescore linescore;
    private final List<PeriodShots> shotsByPeriod;
    private final GameReports gameReports;
    private final GameInfo gameInfo;
}
