package nhlgameupdatelambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import nhlgameupdatelambda.NhlGameUpdateLambda;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import nhlgameupdatelambda.testHelpers.TestContext;
import nhlgameupdatelambda.testHelpers.TestLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NhlGameUpdateHandlerTest {

    private NhlGameTodayLambdaEvent event;

    private NhlGameUpdateHandler underTest;

    private NhlGameTodayLambdaResponse expectedResponse;
    private NhlGameTodayLambdaResponse actualResponse;

    @Before
    public void setup() {
//        underTest = new NhlGameUpdateHandler(new TestLogger(),  new Gson());
        underTest = new NhlGameUpdateHandler(new Gson());
    }

    @After
    public void tearDown() {
        event = null;
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
                .status("OK")
                .statusCode(200)
                .build();
    }

    private void whenHandleRequestIsCalled() {
        actualResponse = underTest.handleRequest(event);
    }

    private void setupEventWithGameId() {
        event = new NhlGameTodayLambdaEvent();
        event.setGameId("1111");
    }
}
