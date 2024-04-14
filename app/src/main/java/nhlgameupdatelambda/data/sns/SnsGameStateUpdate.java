package nhlgameupdatelambda.data.sns;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.common.GameState;

@Builder
@Data
public class SnsGameStateUpdate {
    private final String gameId;
    private final GameState gameState;
}
