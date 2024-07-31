package nhlgameupdatelambda.data.modern.individual;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.modern.individual.player.ModernIndividualGoalie;
import nhlgameupdatelambda.data.modern.individual.player.ModernIndividualSkater;

import java.util.Map;

@Builder
@Data
public class GamePlayers {
    private final Map<Integer, ModernIndividualSkater> skaters;
    private final Map<Integer, ModernIndividualGoalie> goalies;
}
