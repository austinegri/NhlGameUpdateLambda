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
@DynamoDbImmutable(builder = ModernIndividualGoalie.ModernIndividualGoalieBuilder.class)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModernIndividualGoalie extends Player {
    private int shotsAgainst;
    private int saves;
    private int goalsAgainst;
    private float savePct;
    // private int puckFreezes;
//    private float gaa;
    // private float gsaa; ToDo
    // private float xGAgainst; ToDo
//    private int hdShotsAgainst;
//    private int hdSaves;
//    private int hdGoalsAgainst;
//    private float hdSavePct;
//    private float hdGaa;
//    private float hdGsaa;
//    private int rushAttemptsAgainst; ToDo
//    private int reboundAttemptsAgaints; ToDo

    public void incrementShotsAgainst() {
        shotsAgainst += 1;
    }

    public void incrementSaves() {
        saves += 1;
    }

    public void updateSavePct() {
        savePct = (float) saves / shotsAgainst;
    }

    public void incrementGoalsAgainst() {
        goalsAgainst += 1;
    }
}
