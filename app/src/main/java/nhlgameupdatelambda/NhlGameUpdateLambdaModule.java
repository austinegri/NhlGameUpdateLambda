package nhlgameupdatelambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Singleton;

@Module
public class NhlGameUpdateLambdaModule {

    private final Context context;

    public NhlGameUpdateLambdaModule(final Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public LambdaLogger provideLogger() {
        return context.getLogger();
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Provides
    @Singleton
    public ObjectMapper providesObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public Region providesRegion() {
        final String aws_region = System.getenv("AWS_REGION");
        return Region.of(aws_region);
    }

    @Provides
    @Singleton
    public DynamoDbTable<BoxscoreResponse> providesBoxscoreDdbTable(final Region aws_region) {
        final DynamoDbClient ddb = DynamoDbClient.builder()
                .region(aws_region)
                .build();
        final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(ddb)
                .build();
        final String boxscoreTableName = System.getenv("boxscoreTableName");
        return enhancedClient.table(boxscoreTableName,
                TableSchema.fromImmutableClass(BoxscoreResponse.class));
    }

}
