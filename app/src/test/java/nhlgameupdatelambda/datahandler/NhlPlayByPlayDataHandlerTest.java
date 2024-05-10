package nhlgameupdatelambda.datahandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import nhlgameupdatelambda.data.sns.GamePlayUpdate;
import nhlgameupdatelambda.datahandler.impl.NhlPlayByPlayDataHandler;
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

import static nhlgameupdatelambda.testData.TestData.UPDATED_PLAYS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NhlPlayByPlayDataHandlerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String GAME_PLAY_UPDATE_TOPIC_ARN = "gamePlayUpdateTopicArn";
    private String gameId;
    private GameState expectedGameState;
    private GameState actualGameState;
    private PlayByPlay nhlApiPlayByPlay;
    private PlayByPlay ddbPlaybyPlay;
    private NhlData nhlData;

    @Mock
    private NhlApiDao mockNhlApiDao;
    @Mock
    private DdbDao mockDdbDao;
    @Mock
    private SnsClient mockSnsClient;

    private NhlPlayByPlayDataHandler underTest;
    @Before
    public void setUp() throws Exception {
        nhlData = NhlData.builder()
                .build();
        underTest = new NhlPlayByPlayDataHandler(GAME_PLAY_UPDATE_TOPIC_ARN, mockNhlApiDao, mockDdbDao,
                mockSnsClient);
    }

    @After
    public void tearDown() throws Exception {
        underTest = null;
        gameId = null;
        expectedGameState = null;
        actualGameState = null;
        nhlApiPlayByPlay = null;
        ddbPlaybyPlay = null;
        nhlData = null;
    }

    @Test
    public void update_playByPlayReturnsSamePlayByPlays_GameStateOffReturned() throws IOException {
        setGameId();
        setupPlayByPlaysBothOff();
        setupNhlData();
        setupExpectedGameStateOff();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
    }

    @Test
    public void update_playByPlayReturnsUpdatedPlayByPlays_GameStateOffReturned() throws IOException {
        setGameId();
        setupUpdatedNhlApiPlayByPlay();
        setupNhlData();
        setupExpectedGameStateFinal();
        expectSnsClientUpdate();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    @Test
    public void update_nullDdbPlayByPlayReturnsUpdatedPlayByPlays_GameStateOffReturned() throws IOException {
        setGameId();
        setupNullDddbPlayByPlay();
        setupNhlData();
        setupExpectedGameStateFinal();
        expectSnsClientUpdate();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    @Test
    public void update_snsException_GameStateOffReturned() throws IOException {
        setGameId();
        setupUpdatedNhlApiPlayByPlay();
        setupNhlData();
        setupExpectedGameStateFinal();
        expectSnsClientUpdateThrowsException();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    @Test
    public void fetch_getDataFromNhlApi_nhlDataUpdated() throws IOException {
        setGameId();
        setupPlayByPlaysBothOff();
        expectNhlApiDaoReturnsPlayByPlay();
        expectDdbDaoReturnsPlayByPlay();
        whenNhlPlayByPlayHandlerFetchIsCalled();
    }

    private void whenNhlPlayByPlayHandlerIsCalled() {
        actualGameState = underTest.handle(nhlData);
    }

    private void whenNhlPlayByPlayHandlerFetchIsCalled() {
        underTest.fetch(nhlData, gameId);
    }

    private void expectNhlApiDaoReturnsPlayByPlay() throws IOException {
        when(mockNhlApiDao.getPlayByPlay(gameId))
                .thenReturn(nhlApiPlayByPlay);
    }

    private void expectDdbDaoReturnsPlayByPlay() {
        when(mockDdbDao.getPlayByPlay(Integer.parseInt(gameId)))
                .thenReturn(ddbPlaybyPlay);
    }

    private void expectSnsClientUpdate() throws JsonProcessingException {
        final GamePlayUpdate gameUpdate = GamePlayUpdate.builder()
                .gameId(nhlApiPlayByPlay.getId()
                        .toString())
                .newPlays(UPDATED_PLAYS)
                .build();
        final PublishRequest publishRequest = PublishRequest.builder()
                .message(OBJECT_MAPPER.writeValueAsString(gameUpdate))
                .topicArn(GAME_PLAY_UPDATE_TOPIC_ARN)
                .build();
        final PublishResponse publishResponse = PublishResponse.builder()
                .build();
        lenient().when(mockSnsClient.publish(eq(publishRequest)))
                .thenReturn(publishResponse);
    }

    private void expectSnsClientUpdateThrowsException() throws JsonProcessingException {
        final GamePlayUpdate gameUpdate = GamePlayUpdate.builder()
                .gameId(nhlApiPlayByPlay.getId()
                        .toString())
                .newPlays(null)
                .build();
        final PublishRequest publishRequest = PublishRequest.builder()
                .message(OBJECT_MAPPER.writeValueAsString(gameUpdate))
                .topicArn(GAME_PLAY_UPDATE_TOPIC_ARN)
                .build();
        final PublishResponse publishResponse = PublishResponse.builder()
                .build();
        lenient().when(mockSnsClient.publish(eq(publishRequest)))
                .thenThrow(new RuntimeException(""));
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

    private void setupNullDddbPlayByPlay() throws IOException {
        ddbPlaybyPlay = null;
        nhlApiPlayByPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayFinalGameResponse.json"),
                PlayByPlay.class);
    }

    private void setupNhlData() {
        nhlData = NhlData.builder()
                .nhlApiPlayByPlay(nhlApiPlayByPlay)
                .DdbPlayByPlay(ddbPlaybyPlay)
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
