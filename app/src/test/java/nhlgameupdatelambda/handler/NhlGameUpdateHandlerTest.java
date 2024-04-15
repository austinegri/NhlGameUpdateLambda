package nhlgameupdatelambda.handler;

import com.google.gson.*;
import nhlgameupdatelambda.data.common.*;
import nhlgameupdatelambda.model.*;
import nhlgameupdatelambda.orchestrator.*;
import nhlgameupdatelambda.testHelpers.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
