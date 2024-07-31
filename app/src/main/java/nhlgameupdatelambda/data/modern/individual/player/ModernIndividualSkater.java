package nhlgameupdatelambda.data.modern.individual.player;

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

    public void incrementFaceoffsWon() {
        faceoffsWon += 1;
    }

    public void incrementFaceoffsLost() {
        faceoffsLost += 1;
    }

    public void updateFaceoffPct() {
        faceoffPct = (float) faceoffsWon / (faceoffsWon + faceoffsLost);
    }

    public void incrementShotsBlocked() {
        shotsBlocked += 1;
    }

    public void incrementICF() {
        iCF += 1;
    }

    public void incrementIFF() {
        iFF += 1;
    }

    public void incrementShots() {
        shots += 1;
    }

    public void updateShootingPct() {
        shootingPct = (float) goals / shots;
    }

    public void incrementGoals() {
        goals += 1;
    }

    public void incrementFirstAssists() {
        firstAssists += 1;
        incrementTotalAssists();
    }

    public void incrementSecondAssists() {
        secondAssists += 1;
        incrementTotalAssists();
    }

    public void incrementHits() {
        hits += 1;
    }

    public void incrementHitsTaken() {
        hitsTaken += 1;
    }

    public void incrementPim(final int minutes) {
        pim += minutes;
    }

    public void incrementPenaltiesDrawn() {
        penaltiesDrawn += 1;
    }

    public void incrementMinors() {
        minor += 1;
        incrementTotalPenalties();
    }

    public void incrementMajors() {
        major += 1;
        incrementTotalPenalties();
    }

    public void incrementMisconducts() {
        misconduct += 1;
        incrementTotalPenalties();
    }

    public void incrementTakeaways() {
        takeaways += 1;
    }

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
