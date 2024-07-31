package nhlgameupdatelambda.compute.impl;

import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.boxscore.PlayerByGameStats;
import nhlgameupdatelambda.data.boxscore.Roster;
import nhlgameupdatelambda.data.boxscore.player.Skater;
import nhlgameupdatelambda.data.modern.individual.ModernIndividual;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static nhlgameupdatelambda.testData.TestData.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class IndividualStatsTest {

    private static final double EPSILON = 1e-10;

    private static final Roster HOME_ROSTER = Roster.builder()
            .defense(List.of(
                    Skater.builder()
                            .playerId(PLAYERID_1)
                            .build(),
                    Skater.builder()
                            .playerId(PLAYERID_2)
                            .build()))
            .forwards(List.of(Skater.builder()
                            .playerId(PLAYERID_3)
                            .build(),
                    Skater.builder()
                            .playerId(PLAYERID_4)
                            .build()))
            .goalies(List.of(GOALIE_1, GOALIE_2))
            .build();
    private static final Roster AWAY_ROSTER = Roster.builder()
            .defense(List.of(
                    Skater.builder()
                            .playerId(PLAYERID_5)
                            .build(),
                    Skater.builder()
                            .playerId(PLAYERID_6)
                            .build()))
            .forwards(List.of(Skater.builder()
                            .playerId(PLAYERID_7)
                            .build(),
                    Skater.builder()
                            .playerId(PLAYERID_8)
                            .build()))
            .goalies(List.of(GOALIE_3, GOALIE_4))
            .build();

    private IndividualStats underTest;
    private NhlData nhlData;
    private ModernIndividual expectedOutput;
    private ModernIndividual actualOutput;

    @Before
    public void setUp() throws Exception {
        nhlData = NhlData.builder()
                .build();
        setupBoxscoreRosters();
        underTest = new IndividualStats();
    }

    @Test
    public void compute_faceoffs_computeCorrect() {
        setupFaceoffPlays();
        whenComputeIsCalled();
        validateFaceoffs();
    }

    @Test
    public void compute_blockedShots_computeCorrect() {
        setupBlockedShotPlays();
        whenComputeIsCalled();
        validateBlockedShots();
    }

    @Test
    public void compute_shotsOnGoal_computeCorrect() {
        setupShotOnGoalPlays();
        whenComputeIsCalled();
        validateShotsOnGoal();
    }

    @Test
    public void compute_missedShots_computeCorrect() {
        setupMissedShotPlays();
        whenComputeIsCalled();
        validateMissedShots();
    }

    @Test
    public void compute_allShots_computeCorrect() {
        setupAllShotPlays();
        whenComputeIsCalled();
        validateAllShots();
    }

    @Test
    public void compute_hits_computeCorrect() {
        setupHitPlays();
        whenComputeIsCalled();
        validateHits();
    }

    @Test
    public void compute_penalties_computeCorrect() {
        setupPenaltyPlays();
        whenComputeIsCalled();
        validatePenalties();
    }

    @Test
    public void compute_turnovers_computeCorrect() {
        setupTurnoverPlays();
        whenComputeIsCalled();
        validateTurnovers();
    }

    private void whenComputeIsCalled() {
        underTest.compute(nhlData);
        actualOutput = nhlData.getModernIndividual();
    }

    private void setupFaceoffPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(FACEOFF_PLAYS)
                .build());
    }

    private void setupBlockedShotPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(BLOCKED_SHOT_PLAYS)
                .build());
    }

    private void setupShotOnGoalPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(SHOT_PLAYS)
                .build());
    }

    private void setupMissedShotPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(MISSED_SHOT_PLAYS)
                .build());
    }

    private void setupAllShotPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(ALL_SHOT_PLAYS)
                .build());
    }

    private void setupHitPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(HIT_PLAYS)
                .build());
    }

    private void setupPenaltyPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(PENALTY_PLAYS)
                .build());
    }

    private void setupTurnoverPlays() {
        nhlData.setNhlApiPlayByPlay(PlayByPlay.builder()
                .plays(TURNOVER_PLAYS)
                .build());
    }

        private void setupBoxscoreRosters() {
        nhlData.setNhlApiBoxscore(Boxscore.builder()
                .playerByGameStats(PlayerByGameStats.builder()
                        .awayTeam(AWAY_ROSTER)
                        .homeTeam(HOME_ROSTER)
                        .build())
                .build());
    }

    private void validateFaceoffs() {
        final var homeSkater = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getForwards()
                .get(0);
        final var awaySkater = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        assertEquals(2, homeSkater.getFaceoffsWon());
        assertEquals(1, homeSkater.getFaceoffsLost());
        assertEquals((float) 2/3, homeSkater.getFaceoffPct(),  EPSILON);

        assertEquals(1, awaySkater.getFaceoffsWon());
        assertEquals(2, awaySkater.getFaceoffsLost());
        assertEquals((float) 1/3, awaySkater.getFaceoffPct(), EPSILON);
    }

    private void validateBlockedShots() {
        final var homeSkater = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getForwards()
                .get(0);
        final var awaySkater = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        assertEquals(2, homeSkater.getShotsBlocked());
        assertEquals(1, homeSkater.getICF());

        assertEquals(1, awaySkater.getShotsBlocked());
        assertEquals(2, awaySkater.getICF());
    }

    private void validateShotsOnGoal() {
        final var goalie = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getGoalies()
                .get(0);
        final var shooter = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        assertEquals(3, goalie.getShotsAgainst());
        assertEquals(3, goalie.getSaves());
        assertEquals(1., goalie.getSavePct(), EPSILON);

        assertEquals(0, shooter.getGoals());
        assertEquals(3, shooter.getShots());
        assertEquals(3, shooter.getICF());
        assertEquals(3, shooter.getIFF());
        assertEquals(0., shooter.getShootingPct(), EPSILON);
    }

    private void validateMissedShots() {
        final var goalie = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getGoalies()
                .get(0);
        final var shooter = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        assertEquals(0, goalie.getShotsAgainst());
        assertEquals(0, goalie.getSaves());
        assertEquals(0., goalie.getSavePct(), EPSILON);

        assertEquals(0, shooter.getGoals());
        assertEquals(0, shooter.getShots());
        assertEquals(3, shooter.getICF());
        assertEquals(3, shooter.getIFF());
        assertEquals(0., shooter.getShootingPct(), EPSILON);
    }

    private void validateAllShots() {
        final var goalie = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getGoalies()
                .get(0);
        final var shooter = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        final var a1 = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(1);
        final var a2 = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getDefense()
                .get(0);
        final var blocker = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getForwards()
                .get(0);
        assertEquals(4, goalie.getShotsAgainst());
        assertEquals(1, goalie.getSaves());
        assertEquals(.25, goalie.getSavePct(), EPSILON);

        assertEquals(3, shooter.getGoals());
        assertEquals(4, shooter.getShots());
        assertEquals(6, shooter.getICF());
        assertEquals(5, shooter.getIFF());
        assertEquals(.75, shooter.getShootingPct(), EPSILON);

        assertEquals(2, a1.getFirstAssists());
        assertEquals(0, a1.getSecondAssists());
        assertEquals(2, a1.getTotalAssists());

        assertEquals(0, a2.getFirstAssists());
        assertEquals(1, a2.getSecondAssists());
        assertEquals(1, a2.getTotalAssists());

        assertEquals(1, blocker.getShotsBlocked());
    }

    private void validateHits() {
        final var hittee = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getForwards()
                .get(0);
        final var hitter = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        assertEquals(2, hitter.getHits());
        assertEquals(0, hitter.getHitsTaken());

        assertEquals(0, hittee.getHits());
        assertEquals(2, hittee.getHitsTaken());
    }

    private void validatePenalties() {
        final var penaltyTaker = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        final var penaltyDrawer = actualOutput.getPlayerByGameIndividualStats()
                .getHomeTeam()
                .getForwards()
                .get(0);

        assertEquals(3, penaltyTaker.getTotalPenalties());
        assertEquals(1, penaltyTaker.getMinor());
        assertEquals(1, penaltyTaker.getMajor());
        assertEquals(1, penaltyTaker.getMisconduct());
        assertEquals(17, penaltyTaker.getPim());
        assertEquals(0, penaltyTaker.getPenaltiesDrawn());

        assertEquals(2, penaltyDrawer.getPenaltiesDrawn());
        assertEquals(0, penaltyDrawer.getTotalPenalties());
    }

    private void validateTurnovers() {
        final var skater = actualOutput.getPlayerByGameIndividualStats()
                .getAwayTeam()
                .getForwards()
                .get(0);
        assertEquals(2, skater.getTakeaways());
        assertEquals(1, skater.getGiveaways());
    }
}