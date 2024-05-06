package nhlgameupdatelambda.data;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;

@Builder
@Data
public class NhlData {
    private Boxscore nhlApiBoxscore;
    private Boxscore ddbBoxscore;
    private PlayByPlay nhlApiPlayByPlay;
    private PlayByPlay DdbPlayByPlay;
}
