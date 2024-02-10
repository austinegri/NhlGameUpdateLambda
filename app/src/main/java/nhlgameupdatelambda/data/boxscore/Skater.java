package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skater extends Player {
    private final int goals;
    private final int assists;
    private final int points;
    private final int plusMinus;
    private final int hits;
    private final int blockedShots;
    private final int powerPlayGoals;
    private final int powerPlayPoints;
    private final int shorthandedGoals;
    private final int shPoints;
    private final int shots;
    private final String faceoffs;
    private final int faceoffWinningPctg;
    private final String powerPlayToi;
    private final String shorthandedToi;
}
