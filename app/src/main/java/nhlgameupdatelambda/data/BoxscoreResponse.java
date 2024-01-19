package nhlgameupdatelambda.data;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxscoreResponse {
    private final int id;
    private final int season;
    private final int gameType;
    private final String gameDate;
}
