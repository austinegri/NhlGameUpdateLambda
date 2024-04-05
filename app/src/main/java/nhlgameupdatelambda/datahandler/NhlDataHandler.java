package nhlgameupdatelambda.datahandler;

import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.external.NhlApiDao;

public interface NhlDataHandler {
    public GameState handle(final String gameId);
}
