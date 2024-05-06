package nhlgameupdatelambda.datahandler;

import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.common.GameState;

public interface NhlDataHandler {
    void fetch(final NhlData nhlData, final String gameId);
    GameState handle(final NhlData nhlData);
}
