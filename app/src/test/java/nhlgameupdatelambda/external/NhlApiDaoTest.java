package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NhlApiDaoTest {

    private String gameId;
    private NhlApiDao underTest;
    private Boxscore expectedBoxscore;
    private Boxscore actualBoxscore;
    private PlayByPlay expectedPlayByPlay;
    private PlayByPlay actualPlayByPlay;

    @Mock
    private ObjectMapper mockObjectMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false);;

    @Before
    public void setUp() throws Exception {
        underTest = new NhlApiDao(mockObjectMapper);
    }

    @After
    public void tearDown() throws Exception {
        gameId = null;
        underTest = null;
    }

    @Test
    public void getBoxscore_inProgressGameId_boxscoreDataReturned() throws IOException {
        setupInProgressGameId();
        setupExpectedInProgressBoxscore();
        expectObjectMapperOnInProgressBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_critGameId_boxscoreDataReturned() throws IOException {
        setupCritGameId();
        setupExpectedCritBoxscore();
        expectObjectMapperOnCritBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_finalGameId_boxscoreDataReturned() throws IOException {
        setupFinalGameId();
        setupExpectedFinalBoxscore();
        expectObjectMapperOnFinalBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_futGameId_boxscoreDataReturned() throws IOException {
        setupFutureGameId();
        setupExpectedFutBoxscore();
        expectObjectMapperOnFutBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_preGameId_boxscoreDataReturned() throws IOException {
        setupPreGameId();
        setupExpectedPreBoxscore();
        expectObjectMapperOnPreBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_offGameId_boxscoreDataReturned() throws IOException {
        setupOffGameId();
        setupExpectedOffBoxscore();
        expectObjectMapperOnOffBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getPlayByPlay_offGameId_playByPlayReturned() throws IOException {
        setupOffGameId();
        setupExpectedOffPlayByPlay();
        expectObjectMapperOnOffPlayByPlayUrl();
        whenGetPlayByPlayIsCalled();
        verifyPlayByPlay();
    }

    @Test
    public void getPlayByPlay_offOtGameId_playByPlayReturned() throws IOException {
        setupOffGameId();
        setupExpectedOffOtPlayByPlay();
        expectObjectMapperOnOffOtPlayByPlayUrl();
        whenGetPlayByPlayIsCalled();
        verifyPlayByPlay();
    }

    @Test
    public void getPlayByPlay_offShootoutGameId_playByPlayReturned() throws IOException {
        setupOffGameId();
        setupExpectedOffSoPlayByPlay();
        expectObjectMapperOnOffShootoutPlayByPlayUrl();
        whenGetPlayByPlayIsCalled();
        verifyPlayByPlay();
    }

    private void verifyPlayByPlay() {
        assertEquals(expectedPlayByPlay, actualPlayByPlay);
    }

    private void verifyBoxscore() {
        assertEquals(expectedBoxscore, actualBoxscore);
    }

    private void setupExpectedInProgressBoxscore() throws IOException {
        expectedBoxscore = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreLiveGameResponse.json"),
                Boxscore.class);
    }

    private void setupExpectedCritBoxscore() throws IOException {
        expectedBoxscore = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreCritGameResponse.json"),
                Boxscore.class);
    }

    private void setupExpectedFinalBoxscore() throws IOException {
        expectedBoxscore = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFinalGameResponse.json"),
                Boxscore.class);
    }

    private void setupExpectedFutBoxscore() throws IOException {
        expectedBoxscore = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFutGameResponse.json"),
                Boxscore.class);
    }

    private void setupExpectedPreBoxscore() throws IOException {
        expectedBoxscore = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscorePreGameResponse.json"),
                Boxscore.class);
    }

    private void setupExpectedOffBoxscore() throws IOException {
        expectedBoxscore = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                Boxscore.class);
    }

    private void expectObjectMapperOnInProgressBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(Boxscore.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreLiveGameResponse.json"),
                        Boxscore.class));
    }

    private void expectObjectMapperOnCritBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(Boxscore.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreCritGameResponse.json"),
                        Boxscore.class));
    }

    private void expectObjectMapperOnFinalBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(Boxscore.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFinalGameResponse.json"),
                        Boxscore.class));
    }

    private void expectObjectMapperOnFutBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(Boxscore.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFutGameResponse.json"),
                        Boxscore.class));
    }

    private void expectObjectMapperOnPreBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(Boxscore.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscorePreGameResponse.json"),
                        Boxscore.class));
    }

    private void expectObjectMapperOnOffPlayByPlayUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(PlayByPlay.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffGameResponse.json"),
                        PlayByPlay.class));
    }

    private void expectObjectMapperOnOffOtPlayByPlayUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(PlayByPlay.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffOtGameResponse.json"),
                        PlayByPlay.class));
    }
    private void expectObjectMapperOnOffShootoutPlayByPlayUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(PlayByPlay.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffShootoutGameResponse.json"),
                        PlayByPlay.class));
    }

    private void setupExpectedOffPlayByPlay() throws IOException {
        expectedPlayByPlay = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffGameResponse.json"),
                PlayByPlay.class);
    }

    private void setupExpectedOffOtPlayByPlay() throws IOException {
        expectedPlayByPlay = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffOtGameResponse.json"),
                PlayByPlay.class);
    }

    private void setupExpectedOffSoPlayByPlay() throws IOException {
        expectedPlayByPlay = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffShootoutGameResponse.json"),
                PlayByPlay.class);
    }

    private void expectObjectMapperOnOffBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(Boxscore.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                        Boxscore.class));
    }

    private void whenGetBoxscoreIsCalled() {
        actualBoxscore = underTest.getBoxscore(gameId);
    }

    private void whenGetPlayByPlayIsCalled() {
        actualPlayByPlay = underTest.getPlayByPlay(gameId);
    }

    private void setupCritGameId() {
        gameId = "2023020861";
    }

    private void setupPreGameId() {
        gameId = "2023020702";
    }

    private void setupFinalGameId() {
        gameId = "2023020861";
    }

    private void setupInProgressGameId() {
        gameId = "2023020698";
    }

    private void setupFutureGameId() {
        gameId = "2023020702";
    }

    private void setupOffGameId() {
        gameId = "2023020672";
    }
}