package nhlgameupdatelambda.datahandler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.sns.SnsGameStateUpdate;
import nhlgameupdatelambda.datahandler.NhlDataHandler;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
public class NhlBoxscoreDataHandler implements NhlDataHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String gameStateTopicArn;
    private final NhlApiDao nhlApiDao;
    private final DdbDao ddbDao;
    private final SnsClient snsClient;

    @Inject
    public NhlBoxscoreDataHandler(@Named("GameStateTopicArn") final String gameStateTopicArn,
                                  final NhlApiDao nhlApiDao, final DdbDao ddbDao, final SnsClient snsClient) {
        this.gameStateTopicArn = gameStateTopicArn;
        this.nhlApiDao = nhlApiDao;
        this.ddbDao = ddbDao;
        this.snsClient = snsClient;
    }

    @Override
    public void fetch(final NhlData nhlData, final String gameId) {
        // ToDo parallelize
        nhlData.setNhlApiBoxscore(nhlApiDao.getBoxscore(gameId));
        nhlData.setDdbBoxscore(ddbDao.getBoxscore(Integer.parseInt(gameId)));
    }

    @Override
    public GameState handle(final NhlData nhlData) {
        final Boxscore nhlApiBoxscore = nhlData.getNhlApiBoxscore();
        final Boxscore ddbBoxscore = nhlData.getDdbBoxscore();

        if (!nhlApiBoxscore.equals(ddbBoxscore)) {
            ddbDao.putBoxscore(nhlApiBoxscore);

           if (ddbBoxscore == null
                    || nhlApiBoxscore.getGameState() != ddbBoxscore.getGameState()) {
                publishGameStateUpdate(nhlApiBoxscore.getId().toString(), nhlApiBoxscore.getGameState());
            }
        }

        return nhlApiBoxscore.getGameState();
    }

    private void publishGameStateUpdate(final String gameId, final GameState updatedGameState) {
        try {
            final SnsGameStateUpdate snsGameStateUpdate = SnsGameStateUpdate.builder()
                    .gameId(gameId)
                    .gameState(updatedGameState)
                    .build();

            final PublishRequest request = PublishRequest.builder()
                    .message(OBJECT_MAPPER.writeValueAsString(snsGameStateUpdate))
                    .topicArn(gameStateTopicArn)
                    .build();
            final PublishResponse response = snsClient.publish(request);
            log.info("Received response for Sns gameState update for gameId: {} gameState: {} response: {}",
                    gameId, updatedGameState, response);
        } catch (final Exception e) {
            log.error("{} Exception when publishing gameState for gameId: {} with gameState: {}. ",
                    e.getClass(), gameId, updatedGameState, e);
        }
    }
}
