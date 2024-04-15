package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.*;
import nhlgameupdatelambda.data.boxscore.*;
import nhlgameupdatelambda.data.playbyplay.*;
import nhlgameupdatelambda.testHelpers.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

import java.io.*;

import static junit.framework.TestCase.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class NhlApiDaoTest {

    private String gameId;
    private NhlApiDao underTest;
    private BoxscoreResponse expectedBoxscoreResponse;
    private BoxscoreResponse actualBoxscoreResponse;
    private PlayByPlay expectedPlayByPlay;
    private PlayByPlay actualPlayByPlay;

    @Mock
    private ObjectMapper mockObjectMapper;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false);;

    @Before
    public void setUp() throws Exception {
        underTest = new NhlApiDao(new TestLogger(), mockObjectMapper);
    }

    @After
    public void tearDown() throws Exception {
        gameId = null;
        underTest = null;
    }

    @Test
    public void getBoxscore_inProgressGameId_boxscoreDataReturned() throws IOException {
        setupInProgressGameId();
        setupExpectedInProgressBoxscoreResponse();
        expectObjectMapperOnInProgressBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_critGameId_boxscoreDataReturned() throws IOException {
        setupCritGameId();
        setupExpectedCritBoxscoreResponse();
        expectObjectMapperOnCritBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_finalGameId_boxscoreDataReturned() throws IOException {
        setupFinalGameId();
        setupExpectedFinalBoxscoreResponse();
        expectObjectMapperOnFinalBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_futGameId_boxscoreDataReturned() throws IOException {
        setupFutureGameId();
        setupExpectedFutBoxscoreResponse();
        expectObjectMapperOnFutBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_preGameId_boxscoreDataReturned() throws IOException {
        setupPreGameId();
        setupExpectedPreBoxscoreResponse();
        expectObjectMapperOnPreBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyBoxscore();
    }

    @Test
    public void getBoxscore_offGameId_boxscoreDataReturned() throws IOException {
        setupOffGameId();
        setupExpectedOffBoxscoreResponse();
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
        assertEquals(expectedBoxscoreResponse, actualBoxscoreResponse);
    }

    private void setupExpectedInProgressBoxscoreResponse() throws IOException {
        expectedBoxscoreResponse = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreLiveGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void setupExpectedCritBoxscoreResponse() throws IOException {
        expectedBoxscoreResponse = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreCritGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void setupExpectedFinalBoxscoreResponse() throws IOException {
        expectedBoxscoreResponse = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFinalGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void setupExpectedFutBoxscoreResponse() throws IOException {
        expectedBoxscoreResponse = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFutGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void setupExpectedPreBoxscoreResponse() throws IOException {
        expectedBoxscoreResponse = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscorePreGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void setupExpectedOffBoxscoreResponse() throws IOException {
        expectedBoxscoreResponse = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void expectObjectMapperOnInProgressBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(BoxscoreResponse.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreLiveGameResponse.json"),
                        BoxscoreResponse.class));
    }

    private void expectObjectMapperOnCritBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(BoxscoreResponse.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreCritGameResponse.json"),
                        BoxscoreResponse.class));
    }

    private void expectObjectMapperOnFinalBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(BoxscoreResponse.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFinalGameResponse.json"),
                        BoxscoreResponse.class));
    }

    private void expectObjectMapperOnFutBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(BoxscoreResponse.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFutGameResponse.json"),
                        BoxscoreResponse.class));
    }

    private void expectObjectMapperOnPreBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(BoxscoreResponse.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscorePreGameResponse.json"),
                        BoxscoreResponse.class));
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
        when(mockObjectMapper.readValue(any(InputStream.class), eq(BoxscoreResponse.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                        BoxscoreResponse.class));
    }

    private void whenGetBoxscoreIsCalled() {
        actualBoxscoreResponse = underTest.getBoxscore(gameId);
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