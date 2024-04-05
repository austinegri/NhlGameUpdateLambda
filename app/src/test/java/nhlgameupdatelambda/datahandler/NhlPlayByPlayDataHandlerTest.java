package nhlgameupdatelambda.datahandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
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
public class NhlPlayByPlayDataHandlerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String gameId;
    private GameState expectedGameState;
    private GameState actualGameState;
    private PlayByPlay nhlApiPlayByPlay;
    private PlayByPlay ddbPlaybyPlay;

    @Mock
    private NhlApiDao mockNhlApiDao;
    @Mock
    private DdbDao mockDdbDao;

    private NhlPlayByPlayDataHandler underTest;
    @Before
    public void setUp() throws Exception {
        underTest = new NhlPlayByPlayDataHandler(mockNhlApiDao, mockDdbDao);
    }

    @After
    public void tearDown() throws Exception {
        underTest = null;
        gameId = null;
        expectedGameState = null;
        actualGameState = null;
        nhlApiPlayByPlay = null;
        ddbPlaybyPlay = null;
    }

    @Test
    public void update_playByPlayReturnsSamePlayByPlays_GameStateOffReturned() throws IOException {
        setGameId();
        setupPlayByPlaysBothOff();
        setupExpectedGameStateOff();
        expectNhlApiDaoReturnsPlayByPlay();
        expectDdbDaoReturnsPlayByPlay();
        whenNhlGameUpdateOrchestratorIsCalled();
        verifyGameState();
    }

    @Test
    public void update_playByPlayReturnsUpdatedPlayByPlays_GameStateOffReturned() throws IOException {
        setGameId();
        setupUpdatedNhlApiPlayByPlay();
        setupExpectedGameStateFinal();
        expectNhlApiDaoReturnsPlayByPlay();
        expectDdbDaoReturnsPlayByPlay();
        whenNhlGameUpdateOrchestratorIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    private void whenNhlGameUpdateOrchestratorIsCalled() {
        actualGameState = underTest.handle(gameId);
    }

    private void expectNhlApiDaoReturnsPlayByPlay() throws IOException {
        when(mockNhlApiDao.getPlayByPlay(gameId))
                .thenReturn(nhlApiPlayByPlay);
    }

    private void expectDdbDaoReturnsPlayByPlay() {
        when(mockDdbDao.getPlayByPlay(Integer.parseInt(gameId)))
                .thenReturn(ddbPlaybyPlay);
    }

    private void verifyDdbPlayByPlayPutCalled() {
        verify(mockDdbDao, times(1)).putPlayByPlay(nhlApiPlayByPlay);
    }

    private void setGameId() {
        gameId = "1111";
    }

    private void setupPlayByPlaysBothOff() throws IOException {
        nhlApiPlayByPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffGameResponse.json"),
                PlayByPlay.class);
        ddbPlaybyPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffGameResponse.json"),
                PlayByPlay.class);
    }

    private void setupUpdatedNhlApiPlayByPlay() throws IOException {
        ddbPlaybyPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayLiveGameResponse.json"),
                PlayByPlay.class);
        nhlApiPlayByPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayFinalGameResponse.json"),
                PlayByPlay.class);
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
