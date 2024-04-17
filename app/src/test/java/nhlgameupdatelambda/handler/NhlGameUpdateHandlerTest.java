package nhlgameupdatelambda.handler;

import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.model.NhlGameTodayLambdaEvent;
import nhlgameupdatelambda.model.NhlGameTodayLambdaResponse;
import nhlgameupdatelambda.orchestrator.NhlGameUpdateOrchestrator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
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
        underTest = new NhlGameUpdateHandler(mockNhlGameUpdateOrchestrator);
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
