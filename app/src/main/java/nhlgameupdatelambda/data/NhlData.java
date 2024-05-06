package nhlgameupdatelambda.data;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;

@Builder
@Data
public class NhlData {
    private BoxscoreResponse nhlApiBoxscoreResponse;
    private BoxscoreResponse DdbBoxscoreResponse;
    private PlayByPlay nhlApiPlayByPlay;
    private PlayByPlay DdbPlayByPlay;
}
