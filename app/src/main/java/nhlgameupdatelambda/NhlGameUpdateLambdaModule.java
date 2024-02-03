package nhlgameupdatelambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class NhlGameUpdateLambdaModule {

//    private final Context context;

//    public NhlGameUpdateLambdaModule(final Context context) {
//        this.context = context;
//    }
    public NhlGameUpdateLambdaModule(){

    }

//    @Provides
//    @Singleton
//    public LambdaLogger loggerProvider() {
//        return context.getLogger();
//    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

}
