package nhlgameupdatelambda.datahandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import nhlgameupdatelambda.data.NhlData;
import nhlgameupdatelambda.data.common.GameState;
import nhlgameupdatelambda.data.playbyplay.Play;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import nhlgameupdatelambda.data.playbyplay.PlayType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PlayDetail;
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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NhlPlayByPlayDataHandlerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String GAME_PLAY_UPDATE_TOPIC_ARN = "gamePlayUpdateTopicArn";
    private static final String GAME_ID = "1111";
    private static final Play PLAY1 = Play.builder()
            .typeDescKey(PlayType.SHOT_ON_GOAL)
            .details(PlayDetail.builder()
                    .build())
            .build();
    private static final Play PLAY2 = Play.builder()
            .typeDescKey(PlayType.BLOCKED_SHOT)
            .details(PlayDetail.builder()
                    .build())
            .build();
    private static final PlayByPlay PLAY_BY_PLAY1 = PlayByPlay.builder()
            .id(Integer.valueOf(GAME_ID))
            .gameState(GameState.LIVE)
            .plays(ImmutableList.of(PLAY1).stream()
                    .collect(Collectors.toCollection(LinkedHashSet::new)))
            .build();
    private static final PlayByPlay PLAY_BY_PLAY2 = PlayByPlay.builder()
            .id(Integer.valueOf(GAME_ID))
            .gameState(GameState.FINAL)
            .plays(ImmutableList.of(PLAY1,
                            PLAY2)
                    .stream()
                    .collect(Collectors.toCollection(LinkedHashSet::new)))
            .build();

    private static final PlayByPlay PLAY_BY_PLAY3 = PlayByPlay.builder()
            .id(Integer.valueOf(GAME_ID))
            .gameState(GameState.FINAL)
            .plays(ImmutableList.of(PLAY1)
                    .stream()
                    .collect(Collectors.toCollection(LinkedHashSet::new)))
            .build();

    private static final PlayByPlay PLAY_BY_PLAY_NULL_PLAYS = PlayByPlay.builder()
            .id(Integer.valueOf(GAME_ID))
            .gameState(GameState.PRE)
            .build();
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
        expectedGameState = null;
        actualGameState = null;
        nhlApiPlayByPlay = null;
        ddbPlaybyPlay = null;
        nhlData = null;
    }

    @Test
    public void update_playByPlayReturnsSamePlayByPlays_GameStateOffReturned() throws IOException {
        setupPlayByPlaysBothOff();
        setupNhlData();
        setupExpectedGameStateOff();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
    }

    @Test
    public void update_playByPlayReturnsUpdatedPlayByPlays_GameStateOffReturned() throws IOException {
        setupUpdatedNhlApiPlayByPlay();
        setupNhlData();
        setupExpectedGameStateFinal();
        expectSnsClientUpdate();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    @Test
    public void update_noSnsUpdateForSamePlays_GameStateFinalReturned() throws IOException {
        setupUpdatedNhlApiPlayByPlayWithSamePlays();
        setupNhlData();
        setupExpectedGameStateFinal();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    @Test
    public void update_nullDdbPlayByPlayReturnsUpdatedPlayByPlays_GameStateOffReturned() throws IOException {
        setupNullDddbPlayByPlay();
        setupNhlData();
        setupExpectedGameStateFinal();
        expectSnsClientUpdate();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    @Test
    public void update_nullNhlApiPlayByPlayPlays_GameStatePreReturned() throws IOException {
        setupNullPlays();
        setupNhlData();
        setupExpectedGameStatePre();
        whenNhlPlayByPlayHandlerIsCalled();
        verifyGameState();
        verifyDdbPlayByPlayPutCalled();
    }

    @Test
    public void update_snsException_GameStateOffReturned() throws IOException {
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
        setupPlayByPlaysBothOff();
        expectNhlApiDaoReturnsPlayByPlay();
        expectDdbDaoReturnsPlayByPlay();
        whenNhlPlayByPlayHandlerFetchIsCalled();
    }

    private void whenNhlPlayByPlayHandlerIsCalled() {
        actualGameState = underTest.handle(nhlData);
    }

    private void whenNhlPlayByPlayHandlerFetchIsCalled() {
        underTest.fetch(nhlData, GAME_ID);
    }

    private void expectNhlApiDaoReturnsPlayByPlay() throws IOException {
        when(mockNhlApiDao.getPlayByPlay(GAME_ID))
                .thenReturn(nhlApiPlayByPlay);
    }

    private void expectDdbDaoReturnsPlayByPlay() {
        when(mockDdbDao.getPlayByPlay(Integer.parseInt(GAME_ID)))
                .thenReturn(ddbPlaybyPlay);
    }

    private void expectSnsClientUpdate() throws JsonProcessingException {
        final GamePlayUpdate gameUpdate = GamePlayUpdate.builder()
                .gameId(nhlApiPlayByPlay.getId()
                        .toString())
                .newPlays(Set.of(PLAY2))
                .build();
        final PublishRequest publishRequest = PublishRequest.builder()
                .message(OBJECT_MAPPER.writeValueAsString(gameUpdate))
                .topicArn(GAME_PLAY_UPDATE_TOPIC_ARN)
                .build();
        final PublishResponse publishResponse = PublishResponse.builder()
                .build();
        when(mockSnsClient.publish(eq(publishRequest)))
                .thenReturn(publishResponse);
    }

    private void expectSnsClientUpdateThrowsException() throws JsonProcessingException {
        final GamePlayUpdate gameUpdate = GamePlayUpdate.builder()
                .gameId(nhlApiPlayByPlay.getId()
                        .toString())
                .newPlays(Set.of(PLAY2))
                .build();
        final PublishRequest publishRequest = PublishRequest.builder()
                .message(OBJECT_MAPPER.writeValueAsString(gameUpdate))
                .topicArn(GAME_PLAY_UPDATE_TOPIC_ARN)
                .build();
        final PublishResponse publishResponse = PublishResponse.builder()
                .build();
        when(mockSnsClient.publish(eq(publishRequest)))
                .thenThrow(new RuntimeException(""));
    }

    private void verifyDdbPlayByPlayPutCalled() {
        verify(mockDdbDao, times(1)).putPlayByPlay(nhlApiPlayByPlay);
    }

    private void setupPlayByPlaysBothOff() throws IOException {
        nhlApiPlayByPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffGameResponse.json"),
                PlayByPlay.class);
        ddbPlaybyPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffGameResponse.json"),
                PlayByPlay.class);
    }

    private void setupUpdatedNhlApiPlayByPlay() throws IOException {
        ddbPlaybyPlay = PLAY_BY_PLAY1;
        nhlApiPlayByPlay = PLAY_BY_PLAY2;
    }

    private void setupUpdatedNhlApiPlayByPlayWithSamePlays() throws IOException {
        ddbPlaybyPlay = PLAY_BY_PLAY1;
        nhlApiPlayByPlay = PLAY_BY_PLAY3;
    }

    private void setupNullDddbPlayByPlay() throws IOException {
        ddbPlaybyPlay = null;
        nhlApiPlayByPlay = OBJECT_MAPPER.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayFinalGameResponse.json"),
                PlayByPlay.class);
    }

    private void setupNullPlays() throws IOException {
        ddbPlaybyPlay = null;
        nhlApiPlayByPlay = PLAY_BY_PLAY_NULL_PLAYS;
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

    private void setupExpectedGameStatePre() {
        expectedGameState = GameState.PRE;
    }

    private void verifyGameState() {
        assertEquals(expectedGameState, actualGameState);
    }
}
