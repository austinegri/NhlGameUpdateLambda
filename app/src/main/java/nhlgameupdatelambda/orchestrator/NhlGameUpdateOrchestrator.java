package nhlgameupdatelambda.orchestrator;

import nhlgameupdatelambda.data.GameState;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;

import javax.inject.Inject;

public class NhlGameUpdateOrchestrator {

    private final NhlApiDao nhlApiDao;
    private final DdbDao ddbDao;

    @Inject
    public NhlGameUpdateOrchestrator(final NhlApiDao nhlApiDao, final DdbDao ddbDao) {
        this.nhlApiDao = nhlApiDao;
        this.ddbDao = ddbDao;
    }
    public GameState update(final String gameId) {
        final BoxscoreResponse nhlApiBoxscore = nhlApiDao.getBoxscore(gameId);
        final BoxscoreResponse ddbBoxscore = ddbDao.getBoxscore(Integer.parseInt(gameId));

        if(!nhlApiBoxscore.equals(ddbBoxscore)) {
            ddbDao.putBoxscore(nhlApiBoxscore);
        }

        return nhlApiBoxscore.getGameState();
    }
}
