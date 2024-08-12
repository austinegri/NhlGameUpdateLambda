package nhlgameupdatelambda.compute.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.compute.ModernStats;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.boxscore.Roster;
import nhlgameupdatelambda.data.boxscore.player.Goalie;
import nhlgameupdatelambda.data.boxscore.player.Skater;
import nhlgameupdatelambda.data.modern.individual.GamePlayers;
import nhlgameupdatelambda.data.modern.individual.ModernIndividual;
import nhlgameupdatelambda.data.modern.individual.ModernIndividualRoster;
import nhlgameupdatelambda.data.modern.individual.player.ModernIndividualGoalie;
import nhlgameupdatelambda.data.modern.individual.player.ModernIndividualSkater;
import nhlgameupdatelambda.data.modern.individual.PlayerByGameIndividualStats;
import nhlgameupdatelambda.data.playbyplay.Play;
import nhlgameupdatelambda.data.playbyplay.PlayType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PenaltyType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PlayDetail;
import nhlgameupdatelambda.external.DdbDao;

import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nhlgameupdatelambda.data.playbyplay.PlayType.*;

@Slf4j
public class IndividualStats implements ModernStats {

    private final DdbDao ddbDao;

    @Inject
    public IndividualStats(final DdbDao ddbDao) {
        this.ddbDao = ddbDao;
    }

    public void compute(final NhlData nhlData) {
        final Boxscore boxscore = nhlData.getNhlApiBoxscore();
        final LinkedHashSet<Play> plays = nhlData.getNhlApiPlayByPlay()
                .getPlays();
        final GamePlayers gamePlayers = GamePlayers.builder()
                .skaters(Maps.newHashMap())
                .goalies(Maps.newHashMap())
                .build();

        final Roster homeTeam = boxscore.getPlayerByGameStats()
                .getHomeTeam();
        final Roster awayTeam = boxscore.getPlayerByGameStats()
                .getAwayTeam();

        final ModernIndividualRoster homeRoster = getModernIndividualRoster(homeTeam.getForwards(),
                homeTeam.getDefense(), homeTeam.getGoalies(), gamePlayers);
        final ModernIndividualRoster awayRoster = getModernIndividualRoster(awayTeam.getForwards(),
                awayTeam.getDefense(), awayTeam.getGoalies(), gamePlayers);
        nhlData.setModernIndividual(ModernIndividual.builder()
                .id(boxscore.getId())
                .playerByGameIndividualStats(PlayerByGameIndividualStats.builder()
                        .awayTeam(awayRoster)
                        .homeTeam(homeRoster)
                        .build())
                .build());

        addStatsToPlayer(plays, gamePlayers);
    }

    public void save(final NhlData nhlData) {
        ddbDao.putModernIndividual(nhlData.getModernIndividual());
    }

    private ModernIndividualSkater boxscoreToModernIndividual(final Skater skater) {
        return ModernIndividualSkater.builder()
                .playerId(skater.getPlayerId())
                .sweaterNumber(skater.getSweaterNumber())
                .position(skater.getPosition())
                .name(skater.getName())
                .toi(skater.getToi())
                .build();
    }

    private ModernIndividualGoalie boxscoreToModernIndividualGoalie(final Goalie goalie) {
        return ModernIndividualGoalie.builder()
                .playerId(goalie.getPlayerId())
                .sweaterNumber(goalie.getSweaterNumber())
                .position(goalie.getPosition())
                .name(goalie.getName())
                .toi(goalie.getToi())
                .build();
    }

    private ModernIndividualRoster getModernIndividualRoster(final List<Skater> forwards, final List<Skater> defenders,
                                                             final List<Goalie> goalies,
                                                             final GamePlayers gamePlayers) {
        final Map<Integer, ModernIndividualSkater> skaters = gamePlayers.getSkaters();
        final Map<Integer, ModernIndividualGoalie> modernGoalies = gamePlayers.getGoalies();
        return ModernIndividualRoster.builder()
                .forwards(forwards.stream()
                        .map(forward -> {
                            final ModernIndividualSkater skater = boxscoreToModernIndividual(forward);
                            skaters.put(skater.getPlayerId(), skater);
                            return skater;
                        })
                        .collect(Collectors.toList()))
                .defense(defenders.stream()
                        .map(defender -> {
                            final ModernIndividualSkater skater = boxscoreToModernIndividual(defender);
                            skaters.put(skater.getPlayerId(), skater);
                            return skater;
                        })
                        .collect(Collectors.toList()))
                .goalies(goalies.stream()
                        .map(goalie -> {
                            final ModernIndividualGoalie modernGoalie = boxscoreToModernIndividualGoalie(goalie);
                            modernGoalies.put(modernGoalie.getPlayerId(), modernGoalie);
                            return modernGoalie;
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    private void addStatsToPlayer(final LinkedHashSet<Play> plays, final GamePlayers gamePlayers) {
        final Map<Integer, ModernIndividualSkater> skaters = gamePlayers.getSkaters();
        final Map<Integer, ModernIndividualGoalie> goalies = gamePlayers.getGoalies();
        for(final Play play : plays) {
            final PlayType playType = play.getTypeDescKey();
            final PlayDetail detail = play.getDetails();
            if (playType == FACEOFF) {
                final var winningSkater = skaters.get(detail.getWinningPlayerId());
                final var losingSkater = skaters.get(detail.getLosingPlayerId());
                winningSkater.incrementFaceoffsWon();
                winningSkater.updateFaceoffPct();

                losingSkater.incrementFaceoffsLost();
                losingSkater.updateFaceoffPct();
            } else if (playType == STOPPAGE) {
//                if (detail.getReason().equals("goalie-stopped-after-sog")
//                        || detail.getReason().equals("puck-frozen")) {
//                    // ToDo add Goalie Puck Freeze and Save stoppage (goalieId not identified)
//                }

            } else if (playType == BLOCKED_SHOT) {
                final var blockingSkater = skaters.get(detail.getBlockingPlayerId());
                final var shootingSkater = skaters.get(detail.getShootingPlayerId());

                blockingSkater.incrementShotsBlocked();
                shootingSkater.incrementICF();
            } else if (playType == SHOT_ON_GOAL) {
                final var shooter = skaters.get(detail.getShootingPlayerId());
                final var goalie = goalies.get(detail.getGoalieInNetId());
                shooter.incrementICF();
                shooter.incrementIFF();
                shooter.incrementShots();
                shooter.updateShootingPct();

                goalie.incrementShotsAgainst();
                goalie.incrementSaves();
                goalie.updateSavePct();
            } else if (playType == MISSED_SHOT) {
                final var shooter = skaters.get(detail.getShootingPlayerId());
                shooter.incrementICF();
                shooter.incrementIFF();
            } else if (playType == GOAL) {
                final var shooter = skaters.get(detail.getScoringPlayerId());
                final var a1 = skaters.get(detail.getAssist1PlayerId());
                final var a2 = skaters.get(detail.getAssist2PlayerId());
                final var goalie = goalies.get(detail.getGoalieInNetId());

                shooter.incrementGoals();
                shooter.incrementShots();
                shooter.incrementICF();
                shooter.incrementIFF();
                shooter.updateShootingPct();

                if (goalie != null) {
                    // Empty net
                    goalie.incrementShotsAgainst();
                    goalie.incrementGoalsAgainst();
                    goalie.updateSavePct();
                }
                if (a1 != null) {
                    a1.incrementFirstAssists();
                    if (a2 != null) {
                        a2.incrementSecondAssists();
                    }
                }
            } else if (playType == HIT) {
                final var hitter = skaters.get(detail.getHittingPlayerId());
                final var hittee = skaters.get(detail.getHitteePlayerId());

                hitter.incrementHits();
                hittee.incrementHitsTaken();
            } else if (playType == PENALTY) {
                final var penaltyTaker = skaters.get(detail.getCommittedByPlayerId());
                final var penaltyDrawer = skaters.get(detail.getDrawnByPlayerId());
                final var penaltyType = detail.getTypeCode();

                if (penaltyTaker == null) {
                    // Goalies could take penalties
                    continue;
                }

                penaltyTaker.incrementPim(detail.getDuration());
                if (penaltyType == PenaltyType.MIN) {
                    penaltyTaker.incrementMinors();
                } else if (penaltyType == PenaltyType.MAJ) {
                    penaltyTaker.incrementMajors();
                } else if (penaltyType == PenaltyType.GAM) {
                    penaltyTaker.incrementMisconducts();
                }
                if (penaltyDrawer != null) {
                    penaltyDrawer.incrementPenaltiesDrawn();
                }
            } else if (playType == DELAYED_PENALTY) {
                continue;
            } else if (playType == TAKEAWAY) {
                final var skater = skaters.get(detail.getPlayerId());
                skater.incrementTakeaways();
            } else if (playType == GIVEAWAY) {
                final var skater = skaters.get(detail.getPlayerId());
                skater.incrementGiveaways();
            }
        }
    }
}
