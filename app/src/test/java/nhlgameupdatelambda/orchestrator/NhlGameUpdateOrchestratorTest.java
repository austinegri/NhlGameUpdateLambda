package nhlgameupdatelambda.orchestrator;

import com.google.common.collect.*;
import nhlgameupdatelambda.data.common.*;
import nhlgameupdatelambda.datahandler.*;
import nhlgameupdatelambda.testHelpers.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NhlGameUpdateOrchestratorTest {

    private String gameId;
    private GameState expectedGameState;
    private GameState actualGameState;

    private List<NhlDataHandler> nhlDataHandlers;
    @Mock
    private NhlDataHandler mockNhlDataHandler;
    @Mock
    private NhlBoxscoreDataHandler mockNhlBoxscoreDataHandler;

    private NhlGameUpdateOrchestrator underTest;
    @Before
    public void setUp() throws Exception {
        nhlDataHandlers = ImmutableList.of(mockNhlDataHandler,
                mockNhlBoxscoreDataHandler);
        underTest = new NhlGameUpdateOrchestrator(new TestLogger(), nhlDataHandlers);
    }

    @After
    public void tearDown() throws Exception {
        underTest = null;
        gameId = null;
        expectedGameState = null;
        actualGameState = null;
        nhlDataHandlers = null;
        mockNhlDataHandler = null;
        mockNhlBoxscoreDataHandler = null;
    }

    @Test
    public void update_allDataHanldersCalledAndSucceed_GameStateOffReturned() throws IOException {
        setGameId();
        setupExpectedGameStateOff();
        expectAllDataHandlersCalledReturnOff();
        whenNhlGameUpdateOrchestratorIsCalled();
        verifyGameState();
    }

    @Test
    public void update_oneDataHandlerFails_GameStateOffReturned() throws IOException {
        setGameId();
        setupExpectedGameStateOff();
        expectOneDataHandlerThrowsException();
        whenNhlGameUpdateOrchestratorIsCalled();
        verifyGameState();
    }

    @Test
    public void update_allDataHandlersFail_ExceptionThrown() throws IOException {
        setGameId();
        expectAllDataHandlersThrowException();
        assertThrows(RuntimeException.class, () -> whenNhlGameUpdateOrchestratorIsCalled());
    }

    private void whenNhlGameUpdateOrchestratorIsCalled() {
        actualGameState = underTest.update(gameId);
    }

    private void expectAllDataHandlersCalledReturnOff() throws IOException {
        final GameState gameState = GameState.OFF;
        when(mockNhlDataHandler.handle(gameId))
                .thenReturn(gameState);
        when(mockNhlBoxscoreDataHandler.handle(gameId))
                .thenReturn(gameState);
    }

    private void expectOneDataHandlerThrowsException() throws IOException {
        final GameState gameState = GameState.OFF;
        when(mockNhlDataHandler.handle(gameId))
                .thenThrow(new RuntimeException());
        when(mockNhlBoxscoreDataHandler.handle(gameId))
                .thenReturn(gameState);
    }

    private void expectAllDataHandlersThrowException() throws IOException {
        when(mockNhlDataHandler.handle(gameId))
                .thenThrow(new RuntimeException());
        when(mockNhlBoxscoreDataHandler.handle(gameId))
                .thenThrow(new RuntimeException());
    }

    private void setGameId() {
        gameId = "1111";
    }

    private void setupExpectedGameStateOff() {
        expectedGameState = GameState.OFF;
    }

    private void setupExpectedGameStateFinal() {
        expectedGameState = GameState.FINAL;
    }

    private void verifyGameState() {
        assertEquals(expectedGameState, actualGameState);
    }
}