package nhlgameupdatelambda.external;

import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import javax.inject.Inject;

@Slf4j
public class DdbDao {

    private final DynamoDbTable<Boxscore> boxscoreDdbTable;
    private final DynamoDbTable<PlayByPlay> playByPlayDdbTable;

    @Inject
    public DdbDao(final DynamoDbTable<Boxscore> boxscoreDdbTable,
                  final DynamoDbTable<PlayByPlay> playByPlayDdbTable) {
        this.boxscoreDdbTable = boxscoreDdbTable;
        this.playByPlayDdbTable = playByPlayDdbTable;
    }

    public void putBoxscore(final Boxscore boxscoreItem) {
        try {
            log.info("Putting record to BoxscoreTable with GameId: " + boxscoreItem.getId());
            boxscoreDdbTable.putItem(boxscoreItem);
            log.info("Successfully stored record to BoxscoreTable with GameId: " + boxscoreItem.getId());
        } catch (final DynamoDbException e) {
            throw e;
        }
    }

    public Boxscore getBoxscore(final int gameId) {
        try {
            final GetItemEnhancedRequest getItemRequest = GetItemEnhancedRequest.builder()
                    .key(Key.builder()
                            .partitionValue(gameId)
                            .build())
                    .consistentRead(true) // get most recent write value
                    .build();
            log.info("Getting record from BoxscoreTable with GameId: " + gameId);
            final Boxscore boxscoreItem = boxscoreDdbTable.getItem(getItemRequest);
            log.info("Successfully retrieved record from BoxscoreTable with GameId: " + gameId);
            return boxscoreItem;
        } catch (final DynamoDbException e) {
            throw e;
        }
    }
    public void putPlayByPlay(final PlayByPlay playByPlayItem) {
        try {
            log.info("Putting record to PlayByPlayTable with GameId: " + playByPlayItem.getId());
            playByPlayDdbTable.putItem(playByPlayItem);
            log.info("Successfully stored record to PlayByPlayTable with GameId: " + playByPlayItem.getId());
        } catch (final DynamoDbException e) {
            throw e;
        }
    }

    public PlayByPlay getPlayByPlay(final int gameId) {
       try {
            final GetItemEnhancedRequest getItemRequest = GetItemEnhancedRequest.builder()
                    .key(Key.builder()
                            .partitionValue(gameId)
                            .build())
                    .consistentRead(true) // get most recent write value
                    .build();
            log.info("Getting record from PlayByPlayTable with GameId: " + gameId);
            final PlayByPlay playByPlayItem = playByPlayDdbTable.getItem(getItemRequest);
            log.info("Successfully retrieved record from PlayByPlayTable with GameId: " + gameId);
            return playByPlayItem;
        } catch (final DynamoDbException e) {
            throw e;
        }
    }
}
