package nhlgameupdatelambda.datahandler;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import nhlgameupdatelambda.data.boxscore.*;
import nhlgameupdatelambda.data.common.*;
import nhlgameupdatelambda.data.sns.*;
import nhlgameupdatelambda.external.*;
import nhlgameupdatelambda.testHelpers.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;
import software.amazon.awssdk.services.sns.*;
import software.amazon.awssdk.services.sns.model.*;

import java.io.*;

import static org.junit.Assert.*;
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

    @Mock
    private NhlApiDao mockNhlApiDao;
    @Mock
    private DdbDao mockDdbDao;
    @Mock
    private SnsClient mockSnsClient;

    private NhlBoxscoreDataHandler underTest;
    @Before
    public void setUp() throws Exception {
        underTest = new NhlBoxscoreDataHandler(new TestLogger(), GAME_STATE_TOPIC_ARN,
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
    }

    @Test
    public void update_boxscoreReturnsSameBoxscores_GameStateOffReturned() throws IOException {
        setGameId();
        setupBoxscoresBothOff();
        setupExpectedGameStateOff();
        expectNhlApiDaoReturnsBoxscore();
        expectDdbDaoReturnsBoxscore();
        whenNhlBoxscoreHandlerIsCalled();
        verifyGameState();
    }

    @Test
    public void update_boxscoreReturnsUpdatedBoxscores_GameStateOffReturned() throws IOException {
        setGameId();
        setupUpdatedNhlApiBoxscore();
        setupExpectedGameStateFinal();
        expectNhlApiDaoReturnsBoxscore();
        expectDdbDaoReturnsBoxscore();
        expectSnsClientUpdate();
        whenNhlBoxscoreHandlerIsCalled();
        verifyGameState();
        verifyDdbBoxscorePutCalled();
    }

    @Test
    public void update_snsClientThrowsException_GameStateOffReturned() throws IOException {
        setGameId();
        setupUpdatedNhlApiBoxscore();
        setupExpectedGameStateFinal();
        expectNhlApiDaoReturnsBoxscore();
        expectDdbDaoReturnsBoxscore();
        expectSnsClientUpdateThrowsException();
        whenNhlBoxscoreHandlerIsCalled();
        verifyGameState();
        verifyDdbBoxscorePutCalled();
    }

    private void whenNhlBoxscoreHandlerIsCalled() {
        actualGameState = underTest.handle(gameId);
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
