package nhlgameupdatelambda.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.GameState;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NhlGameUpdateOrchestratorTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String gameId;
    private GameState expectedGameState;
    private GameState actualGameState;
    private BoxscoreResponse nhlApiboxscore;
    private BoxscoreResponse ddbBoxscore;

    @Mock
    private NhlApiDao mockNhlApiDao;
    @Mock
    private DdbDao mockDdbDao;

    private NhlGameUpdateOrchestrator underTest;
    @Before
    public void setUp() throws Exception {
        underTest = new NhlGameUpdateOrchestrator(mockNhlApiDao, mockDdbDao);
    }

    @After
    public void tearDown() throws Exception {
        underTest = null;
        gameId = null;
        expectedGameState = null;
        actualGameState = null;
        nhlApiboxscore = null;
        ddbBoxscore = null;
    }

    @Test
    public void update_boxscoreReturnsSameBoxscores_GameStateOffReturned() throws IOException {
        setGameId();
        setupBoxscoresBothOff();
        setupExpectedGameStateOff();
        expectNhlApiDaoReturnsBoxscore();
        expectDdbDaoReturnsBoxscore();
        whenNhlGameUpdateOrchestratorIsCalled();
        verifyGameState();
    }

    @Test
    public void update_boxscoreReturnsUpdatedBoxscores_GameStateOffReturned() throws IOException {
        setGameId();
        setupUpdatedNhlApiBoxscore();
        setupExpectedGameStateFinal();
        expectNhlApiDaoReturnsBoxscore();
        expectDdbDaoReturnsBoxscore();
        whenNhlGameUpdateOrchestratorIsCalled();
        verifyGameState();
        verifyDdbBoxscorePutCalled();
    }

    private void whenNhlGameUpdateOrchestratorIsCalled() {
        actualGameState = underTest.update(gameId);
    }

    private void expectNhlApiDaoReturnsBoxscore() throws IOException {
        when(mockNhlApiDao.getBoxscore(gameId))
                .thenReturn(nhlApiboxscore);
    }

    private void expectDdbDaoReturnsBoxscore() {
        when(mockDdbDao.getBoxscore(Integer.parseInt(gameId)))
                .thenReturn(ddbBoxscore);
    }

    private void verifyDdbBoxscorePutCalled() {
        verify(mockDdbDao, times(1)).putBoxscore(nhlApiboxscore);
    }

    private void setGameId() {
        gameId = "1111";
    }

    private void setupBoxscoresBothOff() throws IOException {
        nhlApiboxscore = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                BoxscoreResponse.class);
        ddbBoxscore = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void setupUpdatedNhlApiBoxscore() throws IOException {
        ddbBoxscore = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreCritGameResponse.json"),
                BoxscoreResponse.class);
        nhlApiboxscore = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFinalGameResponse.json"),
                BoxscoreResponse.class);
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