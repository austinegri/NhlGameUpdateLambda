package nhlgameupdatelambda.testHelpers;

import com.amazonaws.services.lambda.runtime.*;
import org.slf4j.*;

public class TestLogger implements LambdaLogger {
    private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);
    public void log(String message){
        logger.info(message);
    }
    public void log(byte[] message){
        logger.info(new String(message));
    }
}