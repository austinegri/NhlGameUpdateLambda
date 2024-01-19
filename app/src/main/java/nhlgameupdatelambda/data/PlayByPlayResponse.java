package nhlgameupdatelambda.data;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class PlayByPlayResponse {
    private final int id;
    private final int season;
    private final int gameType;
}
