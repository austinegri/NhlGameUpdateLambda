package nhlgameupdatelambda.datahandler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.playbyplay.Play;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import nhlgameupdatelambda.data.sns.GamePlayUpdate;
import nhlgameupdatelambda.datahandler.NhlDataHandler;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedHashSet;

@Slf4j
public class NhlPlayByPlayDataHandler implements NhlDataHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String gamePlayUpdateTopicArn;
    private final NhlApiDao nhlApiDao;
    private final DdbDao ddbDao;
    private final SnsClient snsClient;

    @Inject
    public NhlPlayByPlayDataHandler(@Named("GamePlayUpdateTopicArn") final String gamePlayUpdateTopicArn,
                                    final NhlApiDao nhlApiDao, final DdbDao ddbDao, final SnsClient snsClient) {
        this.gamePlayUpdateTopicArn = gamePlayUpdateTopicArn;
        this.nhlApiDao = nhlApiDao;
        this.ddbDao = ddbDao;
        this.snsClient = snsClient;
    }

    @Override
    public void fetch(final NhlData nhlData, final String gameId) {
        // ToDo parallelize
        nhlData.setNhlApiPlayByPlay(nhlApiDao.getPlayByPlay(gameId));
        nhlData.setDdbPlayByPlay(ddbDao.getPlayByPlay(Integer.parseInt(gameId)));
    }

    @Override
    public GameState handle(final NhlData nhlData) {
        final PlayByPlay nhlApiPlayByPlay = nhlData.getNhlApiPlayByPlay();
        final PlayByPlay ddbPlayByPlay = nhlData.getDdbPlayByPlay();

        if(!nhlApiPlayByPlay.equals(ddbPlayByPlay)) {
            ddbDao.putPlayByPlay(nhlApiPlayByPlay);

            final LinkedHashSet<Play> oldPlays = ddbPlayByPlay == null ? new LinkedHashSet<>() :
                    ddbPlayByPlay.getPlays();
            final Sets.SetView<Play> updatedPlays = getUpdatedPlays(oldPlays, nhlApiPlayByPlay.getPlays());

            if (!updatedPlays.isEmpty()) {
                publishPlayUpdate(nhlApiPlayByPlay.getId().toString(), updatedPlays);
            }
        }

        return nhlApiPlayByPlay.getGameState();
    }

    private Sets.SetView<Play> getUpdatedPlays(final LinkedHashSet<Play> oldPlays, final LinkedHashSet<Play> newPlays) {
        final Sets.SetView<Play> updatedPlays = Sets.difference(newPlays, oldPlays);
        return updatedPlays;
    }

    private void publishPlayUpdate(final String gameId, final Sets.SetView<Play> plays) {
        try {
            final GamePlayUpdate gamePlayUpdate = GamePlayUpdate.builder()
                    .gameId(gameId)
                    .newPlays(plays)
                    .build();

            final PublishRequest request = PublishRequest.builder()
                    .message(OBJECT_MAPPER.writeValueAsString(gamePlayUpdate))
                    .topicArn(gamePlayUpdateTopicArn)
                    .build();
            log.info("Publishing playByPlay update sns for gameId {} and {} new plays", gameId, plays.size());
            snsClient.publish(request);
            log.info("Published Sns gamePlay update for gameId: {}", gameId);
        } catch (final Exception e) {
            log.error("Exception when publishing gameState for gameId: {}", gameId, e);
        }
    }
}
