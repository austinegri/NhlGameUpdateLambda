package nhlgameupdatelambda.datahandler;

import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;

import javax.inject.Inject;

public class NhlPlayByPlayDataHandler implements NhlDataHandler {
    private static final String SNS_TOPIC_ARN = System.getenv("gamePlayUpdateTopicArn");
    private final NhlApiDao nhlApiDao;
    private final DdbDao ddbDao;

    @Inject
    public NhlPlayByPlayDataHandler(final NhlApiDao nhlApiDao, final DdbDao ddbDao) {
        this.nhlApiDao = nhlApiDao;
        this.ddbDao = ddbDao;
    }

    @Override
    public GameState handle(String gameId) {
        final PlayByPlay nhlApiPlayByPlay = nhlApiDao.getPlayByPlay(gameId);
        final PlayByPlay ddbPlayByPlay = ddbDao.getPlayByPlay(Integer.parseInt(gameId));

        if(!nhlApiPlayByPlay.equals(ddbPlayByPlay)) {
            ddbDao.putPlayByPlay(nhlApiPlayByPlay);
        }

        return nhlApiPlayByPlay.getGameState();
    }
}
