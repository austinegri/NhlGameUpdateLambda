package nhlgameupdatelambda.datahandler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.sns.SnsGameStateUpdate;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import javax.inject.Inject;
import javax.inject.Named;

public class NhlBoxscoreDataHandler implements NhlDataHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private final LambdaLogger logger;
    private final String gameStateTopicArn;
    private final NhlApiDao nhlApiDao;
    private final DdbDao ddbDao;
    private final SnsClient snsClient;

    @Inject
    public NhlBoxscoreDataHandler(final LambdaLogger logger,
                                  @Named("GameStateTopicArn") final String gameStateTopicArn,
                                  final NhlApiDao nhlApiDao, final DdbDao ddbDao, final SnsClient snsClient) {
        this.logger = logger;
        this.gameStateTopicArn = gameStateTopicArn;
        this.nhlApiDao = nhlApiDao;
        this.ddbDao = ddbDao;
        this.snsClient = snsClient;
    }

    @Override
    public GameState handle(final String gameId) {
        final BoxscoreResponse nhlApiBoxscore = nhlApiDao.getBoxscore(gameId);
        final BoxscoreResponse ddbBoxscore = ddbDao.getBoxscore(Integer.parseInt(gameId));

        if (!nhlApiBoxscore.equals(ddbBoxscore)) {
            ddbDao.putBoxscore(nhlApiBoxscore);

            if (nhlApiBoxscore.getGameState() != ddbBoxscore.getGameState()) {
                publishGameStateUpdate(gameId, nhlApiBoxscore.getGameState());
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
            final PublishResponse result = snsClient.publish(request);
            logger.log("Published Sns gameState update for gameId: " + gameId + " and gameState: "
                    + updatedGameState);
        } catch (final Exception e) {
            logger.log(e.getClass() + " Exception when publishing gameState for gameId: " + gameId
                    + " with gameState: " + updatedGameState + ". " + e.getMessage(), LogLevel.ERROR);
        }
    }
}
