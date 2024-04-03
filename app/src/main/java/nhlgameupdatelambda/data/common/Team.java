package nhlgameupdatelambda.data.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@SuperBuilder
@DynamoDbImmutable(builder = Team.TeamBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    private final int id;
    private final Name name;
    private final String abbrev;
    private final int score;
    private final int sog;
    private final String logo;
}
