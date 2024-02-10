package nhlgameupdatelambda.orchestrator;

import nhlgameupdatelambda.data.GameState;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.external.NhlApiDao;

import javax.inject.Inject;

public class NhlGameUpdateOrchestrator {

    private final NhlApiDao nhlApiDao;

    @Inject
    public NhlGameUpdateOrchestrator(final NhlApiDao nhlApiDao) {
        this.nhlApiDao = nhlApiDao;
    }
    public GameState update(final String gameId) {
        final BoxscoreResponse boxscoreResponse = nhlApiDao.getBoxscore(gameId);

        return boxscoreResponse.getGameState();
    }
}
