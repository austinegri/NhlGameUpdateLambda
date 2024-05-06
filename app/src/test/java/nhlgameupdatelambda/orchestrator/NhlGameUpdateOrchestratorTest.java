package nhlgameupdatelambda.orchestrator;

import com.google.common.collect.ImmutableList;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.datahandler.NhlDataHandler;
import nhlgameupdatelambda.datahandler.impl.NhlBoxscoreDataHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

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
        underTest = new NhlGameUpdateOrchestrator(nhlDataHandlers);
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
        assertThrows(RuntimeException.class, () -> whenNhlGameUpdateOrchestratorIsCalled());
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
        when(mockNhlDataHandler.handle(any(NhlData.class)))
                .thenReturn(gameState);
        when(mockNhlBoxscoreDataHandler.handle(any(NhlData.class)))
                .thenReturn(gameState);
    }

    private void expectOneDataHandlerThrowsException() throws IOException {
        final GameState gameState = GameState.OFF;
        lenient().when(mockNhlDataHandler.handle(any(NhlData.class)))
                .thenReturn(gameState);
        lenient().when(mockNhlBoxscoreDataHandler.handle(any(NhlData.class)))
                .thenThrow(new RuntimeException());
    }

    private void expectAllDataHandlersThrowException() throws IOException {
        lenient().when(mockNhlDataHandler.handle(any(NhlData.class)))
                .thenThrow(new RuntimeException());
        lenient().when(mockNhlBoxscoreDataHandler.handle(any(NhlData.class)))
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