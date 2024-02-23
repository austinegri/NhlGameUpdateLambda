package nhlgameupdatelambda;

import com.amazonaws.services.lambda.runtime.Context;
import nhlgameupdatelambda.data.GameState;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import nhlgameupdatelambda.testHelpers.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NhlGameUpdateLambdaTest {

    private NhlGameTodayLambdaEvent event;
    private Context context;

    private NhlGameUpdateLambda underTest;

    private NhlGameTodayLambdaResponse expectedResponse;
    private NhlGameTodayLambdaResponse actualResponse;

    @Before
    public void setup() {
        context = new TestContext();
        underTest = new NhlGameUpdateLambda();
    }

    @After
    public void tearDown() {
        event = null;
        context = null;
        expectedResponse = null;
        actualResponse = null;
    }
    @Test
    public void handleRequest_sampleInput_responseNonNull() {
        setupEventWithGameId();
        setupExpectedResponse200Ok();

        whenHandleRequestIsCalled();
        assertEquals(expectedResponse, actualResponse);
    }

    private void setupExpectedResponse200Ok() {
        expectedResponse = NhlGameTodayLambdaResponse.builder()
                .gameState(GameState.OFF)
                .build();
    }

    private void whenHandleRequestIsCalled() {
        actualResponse = underTest.handleRequest(event, context);
    }

    private void setupEventWithGameId() {
        event = new NhlGameTodayLambdaEvent();
        event.setGameId("2023020698");
    }
}
