package nhlgameupdatelambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import nhlgameupdatelambda.data.GameState;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import nhlgameupdatelambda.orchestrator.NhlGameUpdateOrchestrator;

import javax.inject.Inject;


public class NhlGameUpdateHandler {
    private final LambdaLogger logger;

    private final Gson gson;

    private final NhlGameUpdateOrchestrator nhlGameUpdateOrchestrator;

    @Inject
    public NhlGameUpdateHandler(final LambdaLogger logger, final Gson gson,
                                final NhlGameUpdateOrchestrator nhlGameUpdateOrchestrator) {
        this.logger = logger;
        this.gson = gson;
        this.nhlGameUpdateOrchestrator = nhlGameUpdateOrchestrator;
    }

    public NhlGameTodayLambdaResponse handleRequest(final NhlGameTodayLambdaEvent event) {

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());
        final GameState gameState = nhlGameUpdateOrchestrator.update(event.getGameId());

        return NhlGameTodayLambdaResponse.builder()
                .gameState(gameState)
                .build();
    }
}
