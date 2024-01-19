package nhlgameupdatelambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import org.checkerframework.checker.nullness.qual.NonNull;


public class NhlGameUpdateLambda implements RequestHandler<NhlGameTodayLambdaEvent, NhlGameTodayLambdaResponse> {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // ToDo deploy using docker https://docs.aws.amazon.com/lambda/latest/dg/java-image.html#java-image-instructions
    public NhlGameTodayLambdaResponse handleRequest(@NonNull final NhlGameTodayLambdaEvent event, @NonNull final Context context) {
        final LambdaLogger logger = context.getLogger();

        // log execution details
        logger.log("CONTEXT: " + gson.toJson(context));

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        return NhlGameTodayLambdaResponse.builder()
                .status("OK")
                .statusCode(200)
                .build();
    }
}
