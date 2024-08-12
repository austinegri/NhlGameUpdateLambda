package nhlgameupdatelambda.data.modern.individual.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import nhlgameupdatelambda.data.boxscore.player.Player;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

//@Data
@Getter
@SuperBuilder
@DynamoDbImmutable(builder = ModernIndividualSkater.ModernIndividualSkaterBuilder.class)
//@DynamoDbBean
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModernIndividualSkater extends Player {
    private int goals;
    private int totalAssists;
    private int firstAssists;
    private int secondAssists;
    private int totalPoints;
    private int shots;
    private float shootingPct;
    // private final float ixG; ToDo add xG model
    private int iCF;
    private int iFF;
    // private int iSCF;
    // private int iHDCF; // ToDo
    // private int rushAttempts; // ToDo
    // private int reboundsCreated; // ToDo
    private int pim;
    private int totalPenalties;
    private int minor;
    private int major;
    private int misconduct;
    private int penaltiesDrawn;
    private int giveaways;
    private int takeaways;
    private int hits;
    private int hitsTaken;
    private int shotsBlocked;
    private int faceoffsWon;
    private int faceoffsLost;
    private float faceoffPct;

    @DynamoDbIgnore
    public void incrementFaceoffsWon() {
        faceoffsWon += 1;
    }

    @DynamoDbIgnore
    public void incrementFaceoffsLost() {
        faceoffsLost += 1;
    }

    @DynamoDbIgnore
    public void updateFaceoffPct() {
        faceoffPct = (float) faceoffsWon / (faceoffsWon + faceoffsLost);
    }

    @DynamoDbIgnore
    public void incrementShotsBlocked() {
        shotsBlocked += 1;
    }

    @DynamoDbIgnore
    public void incrementICF() {
        iCF += 1;
    }

    @DynamoDbIgnore
    public void incrementIFF() {
        iFF += 1;
    }

    @DynamoDbIgnore
    public void incrementShots() {
        shots += 1;
    }

    @DynamoDbIgnore
    public void updateShootingPct() {
        shootingPct = (float) goals / shots;
    }

    @DynamoDbIgnore
    public void incrementGoals() {
        goals += 1;
    }

    @DynamoDbIgnore
    public void incrementFirstAssists() {
        firstAssists += 1;
        incrementTotalAssists();
    }

    @DynamoDbIgnore
    public void incrementSecondAssists() {
        secondAssists += 1;
        incrementTotalAssists();
    }

    @DynamoDbIgnore
    public void incrementHits() {
        hits += 1;
    }

    @DynamoDbIgnore
    public void incrementHitsTaken() {
        hitsTaken += 1;
    }

    @DynamoDbIgnore
    public void incrementPim(final int minutes) {
        pim += minutes;
    }

    @DynamoDbIgnore
    public void incrementPenaltiesDrawn() {
        penaltiesDrawn += 1;
    }

    @DynamoDbIgnore
    public void incrementMinors() {
        minor += 1;
        incrementTotalPenalties();
    }

    @DynamoDbIgnore
    public void incrementMajors() {
        major += 1;
        incrementTotalPenalties();
    }

    @DynamoDbIgnore
    public void incrementMisconducts() {
        misconduct += 1;
        incrementTotalPenalties();
    }

    @DynamoDbIgnore
    public void incrementTakeaways() {
        takeaways += 1;
    }

    @DynamoDbIgnore
    public void incrementGiveaways() {
        giveaways += 1;
    }

    private void incrementTotalAssists() {
        totalAssists += 1;
    }

    private void incrementTotalPenalties() {
        totalPenalties += 1;
    }
}
