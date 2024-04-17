package nhlgameupdatelambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import org.checkerframework.checker.nullness.qual.NonNull;
import software.amazon.lambda.powertools.logging.Logging;


public class NhlGameUpdateLambda implements RequestHandler<NhlGameTodayLambdaEvent, NhlGameTodayLambdaResponse> {

    @Logging(logEvent = true)
    public NhlGameTodayLambdaResponse handleRequest(@NonNull final NhlGameTodayLambdaEvent event, @NonNull final Context context) {
        final LambdaHandlers handlers = DaggerLambdaHandlers.builder()
                .nhlGameUpdateLambdaModule(new NhlGameUpdateLambdaModule(context))
                .build();
        return handlers.nhlGameUpdateHandler()
                .handleRequest(event);
    }
}
