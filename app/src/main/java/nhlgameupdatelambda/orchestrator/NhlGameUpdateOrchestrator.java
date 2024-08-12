package nhlgameupdatelambda.orchestrator;

import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.compute.impl.IndividualStats;
import nhlgameupdatelambda.data.NhlData;
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
    private final IndividualStats individualStats;

    @Inject
    public NhlGameUpdateOrchestrator(final List<NhlDataHandler> nhlDataHandlers, final IndividualStats individualStats) {
        this.nhlDataHandlers = nhlDataHandlers;
        this.individualStats = individualStats;
    }

    public GameState update(final String gameId) {
        final NhlData nhlData = NhlData.builder()
                .build();

        //ToDo parallelize
        for (final NhlDataHandler nhlDataHandler : nhlDataHandlers) {
            nhlDataHandler.fetch(nhlData, gameId);
        }

        final Set<GameState> gameStateResponses = nhlDataHandlers.parallelStream()
                .map(nhlDataHandler -> {
                    try {
                        return nhlDataHandler.handle(nhlData);
                    } catch (final Exception e) {
                        throw new RuntimeException(String.format("Exception when calling %s.handle for gameId %s",
                                nhlDataHandler.getClass(), gameId), e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        individualStats.compute(nhlData);
        individualStats.save(nhlData); // ToDo only compute and change in case of an update

        return gameStateResponses.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to succeed any data handlers for gameId: " + gameId));
    }
}
