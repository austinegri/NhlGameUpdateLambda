package nhlgameupdatelambda.data.modern.individual;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.boxscore.player.Player;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Data
@SuperBuilder
@DynamoDbImmutable(builder = ModernIndividualSkater.ModernIndividualSkaterBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModernIndividualSkater extends Player {
    private final int goals;
    private final int totalAssists;
    private final int firstAssists;
    private final int secondAssists;
    private final int totalPoints;
    private final int shots;
    private final int shootingPct;
    // private final int ixG; ToDo add xG model
    private final int iCF;
    private final int iFF;
    private final int iSCF;
    private final int iHDCF;
    private final int rushAttempts;
    private final int reboundsCreated;
    private final int pim;
    private final int totalPenalties;
    private final int minor;
    private final int major;
    private final int misconduct;
    private final int penaltiesDrawn;
    private final int giveaways;
    private final int takeaways;
    private final int hits;
    private final int hitsTaken;
    private final int shotsBlocked;
    private final int faceoffsWon;
    private final int faceoffsLost;
    private final int faceoffPct;
}
