package nhlgameupdatelambda.data.playbyplay;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlayType {
    @JsonProperty("period-start")
    PERIOD_START("period-start"),
    @JsonProperty("period-end")
    PERIOD_END("period-end"),
    @JsonProperty("shootout-complete")
    SHOOTOUT_COMPLETE("shootout-complete"),
    @JsonProperty("game-end")
    GAME_END("game-end"),

    // Events
    @JsonProperty("faceoff")
    FACEOFF("faceoff"),
    @JsonProperty("stoppage")
    STOPPAGE("stoppage"),
    @JsonProperty("blocked-shot")
    BLOCKED_SHOT("blocked-shot"),
    @JsonProperty("shot-on-goal")
    SHOT_ON_GOAL("shot-on-goal"),
    @JsonProperty("missed-shot")
    MISSED_SHOT("missed-shot"),
    @JsonProperty("goal")
    GOAL("goal"),
    @JsonProperty("hit")
    HIT("hit"),
    @JsonProperty("penalty")
    PENALTY("penalty"),
    @JsonProperty("delayed-penalty")
    DELAYED_PENALTY("delayed-penalty"),
    @JsonProperty("takeaway")
    TAKEAWAY("takeaway"),
    @JsonProperty("giveaway")
    GIVEAWAY("giveaway");

    public final String playType;
    private PlayType(final String playType) {
        this.playType = playType;
    }

    public String toString() {
        return this.playType;
    }
}
