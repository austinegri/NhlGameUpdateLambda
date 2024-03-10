package nhlgameupdatelambda.external;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import javax.inject.Inject;

public class DdbDao {

    private final LambdaLogger logger;
    private final DynamoDbTable<BoxscoreResponse> boxscoreDdbTable;

    @Inject
    public DdbDao(final LambdaLogger logger, final DynamoDbTable<BoxscoreResponse> boxscoreDdbTable) {
        this.logger = logger;
        this.boxscoreDdbTable = boxscoreDdbTable;
    }

    public void putBoxscore(final BoxscoreResponse boxscoreItem) {
        try {
            logger.log("Putting record to BoxscoreTable with GameId: " + boxscoreItem.getId());
            boxscoreDdbTable.putItem(boxscoreItem);
            logger.log("Successfully stored record to BoxscoreTable with GameId: " + boxscoreItem.getId());
        } catch (final DynamoDbException e) {
            throw e;
        }
    }

    public BoxscoreResponse getBoxscore(final int gameId) {
        try {
            final GetItemEnhancedRequest getItemRequest = GetItemEnhancedRequest.builder()
                    .key(Key.builder()
                            .partitionValue(gameId)
                            .build())
                    .consistentRead(true) // get most recent write value
                    .build();
            logger.log("Getting record from BoxscoreTable with GameId: " + gameId);
            final BoxscoreResponse boxscoreItem = boxscoreDdbTable.getItem(getItemRequest);
            logger.log("Successfully retrieved record from BoxscoreTable with GameId: " + gameId);
            return boxscoreItem;
        } catch (final DynamoDbException e) {
            throw e;
        }
    }
}
