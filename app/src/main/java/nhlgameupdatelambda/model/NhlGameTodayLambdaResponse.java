package nhlgameupdatelambda.model;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.GameState;

@Data
@Builder
public class NhlGameTodayLambdaResponse {
    private final GameState gameState;
}
