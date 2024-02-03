package nhlgameupdatelambda.handler;

import com.google.gson.Gson;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


public class NhlGameUpdateHandler {
    private static final Logger logger = LoggerFactory.getLogger(NhlGameUpdateHandler.class);

    private final Gson gson;

    @Inject
//    public NhlGameUpdateHandler(final LambdaLogger logger, final Gson gson) {
    public NhlGameUpdateHandler(final Gson gson) {
//        this.logger = logger;
        this.gson = gson;
    }

    public NhlGameTodayLambdaResponse handleRequest(final NhlGameTodayLambdaEvent event) {

        // process event
        logger.info("EVENT: " + gson.toJson(event));
        logger.info("EVENT TYPE: " + event.getClass().toString());

        return NhlGameTodayLambdaResponse.builder()
                .status("OK")
                .statusCode(200)
                .build();
    }
}
