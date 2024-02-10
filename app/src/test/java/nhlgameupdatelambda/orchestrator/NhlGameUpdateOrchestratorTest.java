package nhlgameupdatelambda.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.GameState;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.external.NhlApiDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NhlGameUpdateOrchestratorTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String gameId;
    private GameState expectedGameState;
    private GameState actualGameState;
    private BoxscoreResponse boxscoreResponse;

    @Mock
    private NhlApiDao mockNhlApiDao;

    private NhlGameUpdateOrchestrator underTest;
    @Before
    public void setUp() throws Exception {
        underTest = new NhlGameUpdateOrchestrator(mockNhlApiDao);
    }

    @After
    public void tearDown() throws Exception {
        underTest = null;
        gameId = null;
        expectedGameState = null;
        actualGameState = null;
        boxscoreResponse = null;
    }

    @Test
    public void update_boxscoreReturnsOffGame_GameStateOffReturned() throws IOException {
        setGameId();
        setupBoxscore();
        setupExpectedGameStateOff();
        expectNhlApiDaoReturnsBoxscore();
        whenNhlGameUpdateOrchestratorIsCalled();
        verifyGameState();
    }

    private void whenNhlGameUpdateOrchestratorIsCalled() {
        actualGameState = underTest.update(gameId);
    }
    private void expectNhlApiDaoReturnsBoxscore() throws IOException {
        when(mockNhlApiDao.getBoxscore(gameId))
                .thenReturn(boxscoreResponse);
    }

    private void setGameId() {
        gameId = "1111";
    }

    private void setupBoxscore() throws IOException {
        boxscoreResponse = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void setupExpectedGameStateOff() {
        expectedGameState = GameState.OFF;
    }

    private void verifyGameState() {
        assertEquals(expectedGameState, actualGameState);
    }
}