package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@Builder
@DynamoDbImmutable(builder = Broadcast.BroadcastBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Broadcast {
    private final int id;
    private final String market;
    private final String countryCode;
    private final String network;
    private final int sequenceNumber;
}
