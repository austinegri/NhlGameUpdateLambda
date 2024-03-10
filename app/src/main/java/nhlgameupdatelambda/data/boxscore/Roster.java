package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = Roster.RosterBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Roster {
    private final List<Skater> forwards;
    private final List<Skater> defense;
    private final List<Goalie> goalies;
}
