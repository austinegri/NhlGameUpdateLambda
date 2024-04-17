package nhlgameupdatelambda.handler;

import nhlgameupdatelambda.NhlGameUpdateLambda;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import nhlgameupdatelambda.orchestrator.NhlGameUpdateOrchestrator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;


public class NhlGameUpdateHandler {

    private static final Logger log = LogManager.getLogger(NhlGameUpdateLambda.class);

    private final NhlGameUpdateOrchestrator nhlGameUpdateOrchestrator;

    @Inject
    public NhlGameUpdateHandler(final NhlGameUpdateOrchestrator nhlGameUpdateOrchestrator) {
        this.nhlGameUpdateOrchestrator = nhlGameUpdateOrchestrator;
    }

    public NhlGameTodayLambdaResponse handleRequest(final NhlGameTodayLambdaEvent event) {

        // process event
        final GameState gameState = nhlGameUpdateOrchestrator.update(event.getGameId());

        return NhlGameTodayLambdaResponse.builder()
                .gameState(gameState)
                .build();
    }
}
