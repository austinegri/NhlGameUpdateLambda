package nhlgameupdatelambda.testData;

import com.google.common.collect.ImmutableSet;
import nhlgameupdatelambda.data.common.PeriodDescriptor;
import nhlgameupdatelambda.data.common.PeriodType;
import nhlgameupdatelambda.data.playbyplay.Play;
import nhlgameupdatelambda.data.playbyplay.PlayType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PlayDetail;
import nhlgameupdatelambda.data.playbyplay.playdetail.ZoneCode;

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
}
