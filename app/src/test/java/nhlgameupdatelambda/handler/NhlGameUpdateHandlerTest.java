package nhlgameupdatelambda.handler;

import com.google.gson.Gson;
import nhlgameupdatelambda.data.GameState;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import nhlgameupdatelambda.orchestrator.NhlGameUpdateOrchestrator;
import nhlgameupdatelambda.testHelpers.TestLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NhlGameUpdateHandlerTest {

    private NhlGameTodayLambdaEvent event;

    private NhlGameUpdateHandler underTest;

    private NhlGameTodayLambdaResponse expectedResponse;
    private NhlGameTodayLambdaResponse actualResponse;

    @Mock
    private NhlGameUpdateOrchestrator mockNhlGameUpdateOrchestrator;

    @Before
    public void setup() {
        underTest = new NhlGameUpdateHandler(new TestLogger(), new Gson(), mockNhlGameUpdateOrchestrator);
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
        expectNhlGameUpdateOrchestratorReturnsGameState();
        whenHandleRequestIsCalled();
        assertEquals(expectedResponse, actualResponse);
    }

    private void setupExpectedResponse200Ok() {
        expectedResponse = NhlGameTodayLambdaResponse.builder()
                .status("OK")
                .statusCode(200)
                .gameState(GameState.OFF)
                .build();
    }

    private void whenHandleRequestIsCalled() {
        actualResponse = underTest.handleRequest(event);
    }

    private void setupEventWithGameId() {
        event = new NhlGameTodayLambdaEvent();
        event.setGameId("1111");
    }

    private void expectNhlGameUpdateOrchestratorReturnsGameState() {
        when(mockNhlGameUpdateOrchestrator.update(eq(event.getGameId())))
                .thenReturn(GameState.OFF);
    }
}
