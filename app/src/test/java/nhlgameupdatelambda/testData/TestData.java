package nhlgameupdatelambda.testData;

import com.google.common.collect.ImmutableSet;
import nhlgameupdatelambda.data.boxscore.player.Goalie;
import nhlgameupdatelambda.data.common.PeriodDescriptor;
import nhlgameupdatelambda.data.common.PeriodType;
import nhlgameupdatelambda.data.playbyplay.Play;
import nhlgameupdatelambda.data.playbyplay.PlayType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PenaltyType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PlayDetail;
import nhlgameupdatelambda.data.playbyplay.playdetail.ZoneCode;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TestData {
    public static final Play BLOCKED_SHOT = Play.builder()
        .eventId(563)
        .periodDescriptor(PeriodDescriptor.builder()
                .number(3)
                .periodType(PeriodType.REG)
                .build())
        .timeInPeriod("19:38")
        .timeRemaining("00:22")
        .situationCode("0541")
        .homeTeamDefendingSide("left")
        .typeCode(508)
        .typeDescKey(PlayType.BLOCKED_SHOT)
        .sortOrder(784)
        .details(PlayDetail.builder()
                .eventOwnerTeamId(55)
                .xCoord(5)
                .yCoord(-34)
                .zoneCode(ZoneCode.N)
                .blockingPlayerId(8474602)
                .shootingPlayerId(8477015)
                .build())
        .build();
    public static final Play BLOCKED_SHOT_2 = Play.builder()
            .eventId(564)
            .periodDescriptor(PeriodDescriptor.builder()
                    .number(3)
                    .periodType(PeriodType.REG)
                    .build())
            .timeInPeriod("19:54")
            .timeRemaining("00:06")
            .situationCode("0541")
            .homeTeamDefendingSide("left")
            .typeCode(508)
            .typeDescKey(PlayType.BLOCKED_SHOT)
            .sortOrder(785)
            .details(PlayDetail.builder()
                    .eventOwnerTeamId(22)
                    .xCoord(-77)
                    .yCoord(2)
                    .zoneCode(ZoneCode.D)
                    .blockingPlayerId(8479576)
                    .shootingPlayerId(8477416)
                    .build())
            .build();

    public static final Play PERIOD_END = Play.builder()
            .eventId(728)
            .periodDescriptor(PeriodDescriptor.builder()
                    .number(3)
                    .periodType(PeriodType.REG)
                    .build())
            .timeInPeriod("20:00")
            .timeRemaining("00:00")
            .situationCode("0541")
            .homeTeamDefendingSide("left")
            .typeCode(521)
            .typeDescKey(PlayType.PERIOD_END)
            .sortOrder(786)
            .build();

    public static final Play GAME_END = Play.builder()
            .eventId(732)
            .periodDescriptor(PeriodDescriptor.builder()
                    .number(3)
                    .periodType(PeriodType.REG)
                    .build())
            .timeInPeriod("20:00")
            .timeRemaining("00:00")
            .situationCode("0541")
            .homeTeamDefendingSide("left")
            .typeCode(524)
            .typeDescKey(PlayType.GAME_END)
            .sortOrder(790)
            .build();

    public static final Set<Play> UPDATED_PLAYS = ImmutableSet.of(
            BLOCKED_SHOT,
            BLOCKED_SHOT_2,
            PERIOD_END,
            GAME_END
    );

    public static final Integer PLAYERID_1 = 11111;
    public static final Integer PLAYERID_2 = 22222;
    public static final Integer PLAYERID_3 = 33333;
    public static final Integer PLAYERID_4 = 44444;
    public static final Integer PLAYERID_5 = 55555;
    public static final Integer PLAYERID_6 = 66666;
    public static final Integer PLAYERID_7 = 77777;
    public static final Integer PLAYERID_8 = 88888;

    public static final Integer GOALIEID_1 = 10;
    public static final Integer GOALIEID_2 = 20;
    public static final Integer GOALIEID_3 = 30;
    public static final Integer GOALIEID_4 = 40;

    public static final Goalie GOALIE_1 = Goalie.builder()
            .playerId(GOALIEID_1)
            .build();
    public static final Goalie GOALIE_2 = Goalie.builder()
            .playerId(GOALIEID_2)
            .build();
    public static final Goalie GOALIE_3 = Goalie.builder()
            .playerId(GOALIEID_3)
            .build();
    public static final Goalie GOALIE_4 = Goalie.builder()
            .playerId(GOALIEID_4)
            .build();

    public static final LinkedHashSet<Play> FACEOFF_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.FACEOFF)
                    .details(PlayDetail.builder()
                            .winningPlayerId(PLAYERID_3)
                            .losingPlayerId(PLAYERID_7)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.FACEOFF)
                    .details(PlayDetail.builder()
                            .winningPlayerId(PLAYERID_3)
                            .losingPlayerId(PLAYERID_7)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(3)
                    .typeDescKey(PlayType.FACEOFF)
                    .details(PlayDetail.builder()
                            .winningPlayerId(PLAYERID_7)
                            .losingPlayerId(PLAYERID_3)
                            .build())
                    .build()));

    public static final LinkedHashSet<Play> BLOCKED_SHOT_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.BLOCKED_SHOT)
                    .details(PlayDetail.builder()
                            .blockingPlayerId(PLAYERID_3)
                            .shootingPlayerId(PLAYERID_7)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.BLOCKED_SHOT)
                    .details(PlayDetail.builder()
                            .blockingPlayerId(PLAYERID_3)
                            .shootingPlayerId(PLAYERID_7)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(3)
                    .typeDescKey(PlayType.BLOCKED_SHOT)
                    .details(PlayDetail.builder()
                            .blockingPlayerId(PLAYERID_7)
                            .shootingPlayerId(PLAYERID_3)
                            .build())
                    .build()));

    public static final LinkedHashSet<Play> SHOT_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.SHOT_ON_GOAL)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.SHOT_ON_GOAL)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(3)
                    .typeDescKey(PlayType.SHOT_ON_GOAL)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build()));

    public static final LinkedHashSet<Play> MISSED_SHOT_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.MISSED_SHOT)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.MISSED_SHOT)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(3)
                    .typeDescKey(PlayType.MISSED_SHOT)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build()));

    public static final LinkedHashSet<Play> ALL_SHOT_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.SHOT_ON_GOAL)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.MISSED_SHOT)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(3)
                    .typeDescKey(PlayType.GOAL)
                    .details(PlayDetail.builder()
                            .scoringPlayerId(PLAYERID_7)
                            .assist1PlayerId(PLAYERID_8)
                            .assist2PlayerId(PLAYERID_5)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(4)
                    .typeDescKey(PlayType.BLOCKED_SHOT)
                    .details(PlayDetail.builder()
                            .shootingPlayerId(PLAYERID_7)
                            .blockingPlayerId(PLAYERID_3)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(5)
                    .typeDescKey(PlayType.GOAL)
                    .details(PlayDetail.builder()
                            .scoringPlayerId(PLAYERID_7)
                            .assist1PlayerId(PLAYERID_8)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(6)
                    .typeDescKey(PlayType.GOAL)
                    .details(PlayDetail.builder()
                            .scoringPlayerId(PLAYERID_7)
                            .goalieInNetId(GOALIEID_1)
                            .build())
                    .build()));

    public static final LinkedHashSet<Play> HIT_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.HIT)
                    .details(PlayDetail.builder()
                            .hittingPlayerId(PLAYERID_7)
                            .hitteePlayerId(PLAYERID_3)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.HIT)
                    .details(PlayDetail.builder()
                            .hittingPlayerId(PLAYERID_7)
                            .hitteePlayerId(PLAYERID_3)
                            .build())
                    .build()));

    public static final LinkedHashSet<Play> PENALTY_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.PENALTY)
                    .details(PlayDetail.builder()
                            .committedByPlayerId(PLAYERID_7)
                            .drawnByPlayerId(PLAYERID_3)
                            .typeCode(PenaltyType.MIN)
                            .duration(2)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.PENALTY)
                    .details(PlayDetail.builder()
                            .committedByPlayerId(PLAYERID_7)
                            .drawnByPlayerId(PLAYERID_3)
                            .typeCode(PenaltyType.MAJ)
                            .duration(5)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(3)
                    .typeDescKey(PlayType.PENALTY)
                    .details(PlayDetail.builder()
                            .committedByPlayerId(PLAYERID_7)
                            .typeCode(PenaltyType.GAM)
                            .duration(10)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(5)
                    .typeDescKey(PlayType.DELAYED_PENALTY)
                    .details(PlayDetail.builder()
                            .eventOwnerTeamId(22)
                            .build())
                    .build()));

    public static final LinkedHashSet<Play> TURNOVER_PLAYS = new LinkedHashSet<Play>(List.of(
            Play.builder()
                    .eventId(1)
                    .typeDescKey(PlayType.TAKEAWAY)
                    .details(PlayDetail.builder()
                            .playerId(PLAYERID_7)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(2)
                    .typeDescKey(PlayType.TAKEAWAY)
                    .details(PlayDetail.builder()
                            .playerId(PLAYERID_7)
                            .build())
                    .build(),
            Play.builder()
                    .eventId(3)
                    .typeDescKey(PlayType.GIVEAWAY)
                    .details(PlayDetail.builder()
                            .playerId(PLAYERID_7)
                            .build())
                    .build()));
}
