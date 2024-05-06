package nhlgameupdatelambda.datahandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.sns.SnsGameStateUpdate;
import nhlgameupdatelambda.datahandler.impl.NhlBoxscoreDataHandler;
import nhlgameupdatelambda.external.DdbDao;
import nhlgameupdatelambda.external.NhlApiDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NhlBoxscoreDataHandlerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String GAME_STATE_TOPIC_ARN = "gameStateTopicArn";
    private String gameId;
    private GameState expectedGameState;
    private GameState actualGameState;
    private BoxscoreResponse nhlApiboxscore;
    private BoxscoreResponse ddbBoxscore;
    private NhlData nhlData;

    @Mock
    private NhlApiDao mockNhlApiDao;
    @Mock
    private DdbDao mockDdbDao;
    @Mock
    private SnsClient mockSnsClient;

    private NhlBoxscoreDataHandler underTest;
    @Before
    public void setUp() throws Exception {
        nhlData = NhlData.builder()
                .build();
        underTest = new NhlBoxscoreDataHandler(GAME_STATE_TOPIC_ARN,
                mockNhlApiDao, mockDdbDao, mockSnsClient);
    }

    @After
    public void tearDown() throws Exception {
        underTest = null;
        gameId = null;
        expectedGameState = null;
        actualGameState = null;
        nhlApiboxscore = null;
        ddbBoxscore = null;
        nhlData = null;
    }

    @Test
    public void handle_boxscoreReturnsSameBoxscores_GameStateOffReturned() throws IOException {
        setupBoxscoresBothOff();
        setupNhlData();
        setupExpectedGameStateOff();
        whenNhlBoxscoreHandlerHandleIsCalled();
        verifyGameState();
    }

    @Test
    public void handle_boxscoreReturnsUpdatedBoxscores_GameStateOffReturned() throws IOException {
        setupUpdatedNhlApiBoxscore();
        setupNhlData();
        setupExpectedGameStateFinal();
        expectSnsClientUpdate();
        whenNhlBoxscoreHandlerHandleIsCalled();
        verifyGameState();
        verifyDdbBoxscorePutCalled();
    }

    @Test
    public void handle_snsClientThrowsException_GameStateOffReturned() throws IOException {
        setupUpdatedNhlApiBoxscore();
        setupNhlData();
        setupExpectedGameStateFinal();
        expectSnsClientUpdateThrowsException();
        whenNhlBoxscoreHandlerHandleIsCalled();
        verifyGameState();
        verifyDdbBoxscorePutCalled();
    }

    @Test public void fetch_getDataFromNhlApi_nhlDataUpdated() throws IOException {
        setGameId();
        setupBoxscoresBothOff();
        expectNhlApiDaoReturnsBoxscore();
        expectDdbDaoReturnsBoxscore();
        whenNhlBoxscoreHandlerFetchIsCalled();
    }

    private void whenNhlBoxscoreHandlerHandleIsCalled() {
        actualGameState = underTest.handle(nhlData);
    }

    private void whenNhlBoxscoreHandlerFetchIsCalled() {
        underTest.fetch(nhlData, gameId);
    }

    private void expectNhlApiDaoReturnsBoxscore() throws IOException {
        when(mockNhlApiDao.getBoxscore(gameId))
                .thenReturn(nhlApiboxscore);
    }

    private void expectDdbDaoReturnsBoxscore() {
        when(mockDdbDao.getBoxscore(Integer.parseInt(gameId)))
                .thenReturn(ddbBoxscore);
    }

    private void expectSnsClientUpdate() throws JsonProcessingException {
        final SnsGameStateUpdate gameUpdate = SnsGameStateUpdate.builder()
                .gameId(nhlApiboxscore.getId()
                        .toString())
                .gameState(expectedGameState)
                .build();
        final PublishRequest publishRequest = PublishRequest.builder()
                .message(OBJECT_MAPPER.writeValueAsString(gameUpdate))
                .topicArn(GAME_STATE_TOPIC_ARN)
                .build();
        final PublishResponse publishResponse = PublishResponse.builder()
                .build();
        lenient().when(mockSnsClient.publish(eq(publishRequest)))
                .thenReturn(publishResponse);
    }

    private void expectSnsClientUpdateThrowsException() throws JsonProcessingException {
        final SnsGameStateUpdate gameUpdate = SnsGameStateUpdate.builder()
                .gameId(nhlApiboxscore.getId()
                        .toString())
                .gameState(expectedGameState)
                .build();
        final PublishRequest publishRequest = PublishRequest.builder()
                .message(OBJECT_MAPPER.writeValueAsString(gameUpdate))
                .topicArn(GAME_STATE_TOPIC_ARN)
                .build();
        final PublishResponse publishResponse = PublishResponse.builder()
                .build();
        lenient().when(mockSnsClient.publish(eq(publishRequest)))
                .thenThrow(new RuntimeException(""));
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

    private void setupNhlData() {
        nhlData = NhlData.builder()
                .nhlApiBoxscoreResponse(nhlApiboxscore)
                .DdbBoxscoreResponse(ddbBoxscore)
                .build();
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
