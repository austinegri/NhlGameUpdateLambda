package nhlgameupdatelambda.orchestrator;

import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.datahandler.NhlDataHandler;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class NhlGameUpdateOrchestrator {

    private final List<NhlDataHandler> nhlDataHandlers;

    @Inject
    public NhlGameUpdateOrchestrator(final List<NhlDataHandler> nhlDataHandlers) {
        this.nhlDataHandlers = nhlDataHandlers;
    }

    public GameState update(final String gameId) {
        final Set<GameState> gameStateResponses = nhlDataHandlers.parallelStream()
                .map(nhlDataHandler -> {
                    try {
                        return nhlDataHandler.handle(gameId);
                    } catch (final Exception e) {
                        throw new RuntimeException(String.format("Exception when calling %s.handle for gameId %s",
                                nhlDataHandler.getClass(), gameId), e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return gameStateResponses.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to succeed any data handlers for gameId: " + gameId));
    }
}
