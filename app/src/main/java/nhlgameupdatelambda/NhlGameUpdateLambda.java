package nhlgameupdatelambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import org.checkerframework.checker.nullness.qual.NonNull;


public class NhlGameUpdateLambda implements RequestHandler<NhlGameTodayLambdaEvent, NhlGameTodayLambdaResponse> {

    // ToDo deploy using docker https://docs.aws.amazon.com/lambda/latest/dg/java-image.html#java-image-instructions
    public NhlGameTodayLambdaResponse handleRequest(@NonNull final NhlGameTodayLambdaEvent event, @NonNull final Context context) {
        final LambdaHandlers handlers = DaggerLambdaHandlers.builder()
                .nhlGameUpdateLambdaModule(new NhlGameUpdateLambdaModule(context))
                .build();
        return handlers.nhlGameUpdateHandler()
                .handleRequest(event);
    }
}
