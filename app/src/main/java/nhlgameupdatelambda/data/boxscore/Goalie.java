package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Goalie extends Player {
    private final String evenStrengthShotsAgainst;
    private final String powerPlayShotsAgainst;
    private final String shorthandedShotsAgainst;
    private final String saveShotsAgainst;
    private final int evenStrengthGoalsAgainst;
    private final int powerPlayGoalsAgainst;
    private final int shorthandedGoalsAgainst;
    private final int goalsAgainst;
}
