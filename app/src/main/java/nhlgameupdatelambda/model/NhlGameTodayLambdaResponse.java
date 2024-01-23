package nhlgameupdatelambda.model;

import lombok.Builder;
import lombok.Data;
import nhlgameupdatelambda.data.GameState;

@Data
@Builder
public class NhlGameTodayLambdaResponse {
    private final String status;
    private final int statusCode;
    private final GameState gameState;
}
