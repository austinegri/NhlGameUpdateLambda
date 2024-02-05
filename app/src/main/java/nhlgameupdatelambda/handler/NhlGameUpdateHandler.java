package nhlgameupdatelambda.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;

import javax.inject.Inject;


public class NhlGameUpdateHandler {
    private final LambdaLogger logger;

    private final Gson gson;

    @Inject
    public NhlGameUpdateHandler(final LambdaLogger logger, final Gson gson) {
        this.logger = logger;
        this.gson = gson;
    }

    public NhlGameTodayLambdaResponse handleRequest(final NhlGameTodayLambdaEvent event) {

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());

        return NhlGameTodayLambdaResponse.builder()
                .status("OK")
                .statusCode(200)
                .build();
    }
}
