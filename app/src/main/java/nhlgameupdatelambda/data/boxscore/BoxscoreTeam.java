package nhlgameupdatelambda.data.boxscore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxscoreTeam {
    private final int id;
    private final Name name;
    private final String abbrev;
    private final int score;
    private final int sog;
    private final int faceoffWinningPctg;
    private final String powerPlayConversion;
    private final int pim;
    private final int hits;
    private final int blocks;
    private final String logo;
}
