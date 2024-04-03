package nhlgameupdatelambda.data.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = TeamGameStats.TeamGameStatsBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamGameStats {
    private final Stat category;
    private final int awayValue;
    private final int homeValue;
}
