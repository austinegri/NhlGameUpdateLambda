package nhlgameupdatelambda.data.playbyplay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.common.PeriodDescriptor;
import nhlgameupdatelambda.data.playbyplay.playdetail.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnoreNulls;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;


@Data
@Builder
@DynamoDbImmutable(builder = Play.PlayBuilder.class)
@Jacksonized
//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Play {
    private final int eventId;
    private final PeriodDescriptor periodDescriptor;
    private final String timeInPeriod;
    private final String timeRemaining;
    private final String situationCode;
    private final String homeTeamDefendingSide;
    private final int typeCode;
    private final PlayType typeDescKey;
    private final int sortOrder;
    @Getter(onMethod=@__(@DynamoDbIgnoreNulls))
    private final PlayDetail details;
}
