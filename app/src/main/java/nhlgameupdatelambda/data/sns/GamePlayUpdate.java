package nhlgameupdatelambda.data.sns;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.playbyplay.Play;

import java.util.Set;

@Builder
@Data
public class GamePlayUpdate {
    private final String gameId;
    private final Set<Play> newPlays;
}
