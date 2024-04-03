package nhlgameupdatelambda.data.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Stat {
    @JsonProperty("sog")
    SOG,
    @JsonProperty("faceoffWinningPctg")
    FACEOFF_WIN_PCT,
    @JsonProperty("powerPlay")
    POWER_PLAY
}
