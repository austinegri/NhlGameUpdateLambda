package nhlgameupdatelambda.compute.impl;

import nhlgameupdatelambda.compute.ModernStats;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.boxscore.player.Skater;
import nhlgameupdatelambda.data.modern.individual.ModernIndividualRoster;
import nhlgameupdatelambda.data.modern.individual.ModernIndividualSkater;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IndividualModernStats implements ModernStats {

    public void compute(final NhlData nhlData) {
        final Boxscore boxscore = nhlData.getNhlApiBoxscore();

        final ModernIndividualRoster homeRoster = getModernIndividualRoster(boxscore.getPlayerByGameStats()
                        .getHomeTeam()
                        .getForwards(),
                boxscore.getPlayerByGameStats()
                        .getHomeTeam()
                        .getDefense());
        final ModernIndividualRoster awayRoster = getModernIndividualRoster(boxscore.getPlayerByGameStats()
                        .getAwayTeam()
                        .getForwards(),
                boxscore.getPlayerByGameStats()
                        .getAwayTeam()
                        .getDefense());

    }

    private ModernIndividualSkater boxscoreToModernIndividual(final Skater skater) {
        return ModernIndividualSkater.builder()
                .playerId(skater.getPlayerId())
                .sweaterNumber(skater.getSweaterNumber())
                .position(skater.getPosition())
                .name(skater.getName())
                .pim(skater.getPim())
                .toi(skater.getToi())
                .build();
    }

    private ModernIndividualRoster getModernIndividualRoster(final List<Skater> forwards, final List<Skater> defenders) {
        return ModernIndividualRoster.builder()
                .forwards(forwards.stream()
                        .map(forward -> boxscoreToModernIndividual(forward))
                        .collect(Collectors.toMap(ModernIndividualSkater::getPlayerId, Function.identity())))
                .defense(defenders.stream()
                        .map(defender -> boxscoreToModernIndividual(defender))
                        .collect(Collectors.toMap(ModernIndividualSkater::getPlayerId, Function.identity())))
                //ToDo add goalies
                .build();
    }
}
