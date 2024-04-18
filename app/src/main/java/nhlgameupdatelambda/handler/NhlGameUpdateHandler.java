package nhlgameupdatelambda.handler;

import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import nhlgameupdatelambda.orchestrator.NhlGameUpdateOrchestrator;

import javax.inject.Inject;


@Slf4j
public class NhlGameUpdateHandler {

    private final NhlGameUpdateOrchestrator nhlGameUpdateOrchestrator;

    @Inject
    public NhlGameUpdateHandler(final NhlGameUpdateOrchestrator nhlGameUpdateOrchestrator) {
        this.nhlGameUpdateOrchestrator = nhlGameUpdateOrchestrator;
    }

    public NhlGameTodayLambdaResponse handleRequest(final NhlGameTodayLambdaEvent event) {
        final GameState gameState = nhlGameUpdateOrchestrator.update(event.getGameId());

        log.info("Returning GameState {}", gameState);
        return NhlGameTodayLambdaResponse.builder()
                .gameState(gameState)
                .build();
    }
}
