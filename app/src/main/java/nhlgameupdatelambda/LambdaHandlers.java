package nhlgameupdatelambda;


import dagger.Component;
import nhlgameupdatelambda.handler.NhlGameUpdateHandler;

import javax.inject.Singleton;

// https://www.jvt.me/posts/2021/10/19/dagger-java-lambda/
@Singleton
@Component(modules={
        NhlGameUpdateLambdaModule.class
})
public interface LambdaHandlers {
    NhlGameUpdateHandler nhlGameUpdateHandler();
}
