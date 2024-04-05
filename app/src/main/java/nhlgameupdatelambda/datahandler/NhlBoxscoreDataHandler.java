package nhlgameupdatelambda.datahandler;

import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;

import javax.inject.Inject;

public class NhlBoxscoreDataHandler implements NhlDataHandler {

    private final NhlApiDao nhlApiDao;
    private final DdbDao ddbDao;

    @Inject
    public NhlBoxscoreDataHandler(final NhlApiDao nhlApiDao, final DdbDao ddbDao) {
        this.nhlApiDao = nhlApiDao;
        this.ddbDao = ddbDao;
    }

    @Override
    public GameState handle(final String gameId) {
        final BoxscoreResponse nhlApiBoxscore = nhlApiDao.getBoxscore(gameId);
        final BoxscoreResponse ddbBoxscore = ddbDao.getBoxscore(Integer.parseInt(gameId));

        if(!nhlApiBoxscore.equals(ddbBoxscore)) {
            ddbDao.putBoxscore(nhlApiBoxscore);
        }

        return nhlApiBoxscore.getGameState();
    }
}
