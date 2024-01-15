package nhlgameupdatelambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import org.checkerframework.checker.nullness.qual.NonNull;


public class NhlGameUpdateLambda implements RequestHandler<NhlGameTodayLambdaEvent, String> {
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // ToDo deploy using docker https://docs.aws.amazon.com/lambda/latest/dg/java-image.html#java-image-instructions
    public String handleRequest(@NonNull final NhlGameTodayLambdaEvent event, @NonNull final Context context) {
        final LambdaLogger logger = context.getLogger();
        final String response = "200 OK";

        // log execution details
        logger.log("CONTEXT: " + gson.toJson(context));

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        return response;
    }
}
