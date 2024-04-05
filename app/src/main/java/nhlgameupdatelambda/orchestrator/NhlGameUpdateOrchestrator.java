package nhlgameupdatelambda.orchestrator;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.datahandler.NhlDataHandler;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class NhlGameUpdateOrchestrator {

    private final LambdaLogger logger;
    private final List<NhlDataHandler> nhlDataHandlers;

    @Inject
    public NhlGameUpdateOrchestrator(final LambdaLogger logger, final List<NhlDataHandler> nhlDataHandlers) {
        this.logger = logger;
        this.nhlDataHandlers = nhlDataHandlers;
    }
    public GameState update(final String gameId) {
        final Set<GameState> gameStateResponses = nhlDataHandlers.parallelStream()
                .map(nhlDataHandler -> {
                    try {
                        return nhlDataHandler.handle(gameId);
                    } catch (final Exception e) {
                        logger.log("Exception when calling " + nhlDataHandler.getClass() + ".handle for gameId "
                                + gameId, LogLevel.ERROR);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return gameStateResponses.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to succeed any data handlers for gameId: " + gameId));
    }
}
