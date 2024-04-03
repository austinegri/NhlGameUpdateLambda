package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.boxscore.player.Goalie;
import nhlgameupdatelambda.data.boxscore.player.Skater;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Data
@Builder
@DynamoDbImmutable(builder = Roster.RosterBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Roster {
    private final List<Skater> forwards;
    private final List<Skater> defense;
    private final List<Goalie> goalies;
}
