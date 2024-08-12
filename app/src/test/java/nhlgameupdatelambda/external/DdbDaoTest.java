package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.boxscore.Boxscore;
import nhlgameupdatelambda.data.modern.individual.ModernIndividual;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DdbDaoTest {

    private int gameId;
    private Boxscore boxscoreItem;
    private PlayByPlay playByPlayItem;
    private GetItemEnhancedRequest getItemRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DynamoDbTable<Boxscore> mockBoxscoreTable;
    @Mock
    private DynamoDbTable<PlayByPlay> mockPlayByPlayTable;
    @Mock
    private DynamoDbTable<ModernIndividual> mockModernIndividualTable;

    private DdbDao underTest;
    private Boxscore actualBoxscore;
    private PlayByPlay actualPlayByPlayResponse;

    @Before
    public void setUp() {
        gameId = 2023020900;
        underTest = new DdbDao(mockBoxscoreTable, mockPlayByPlayTable, mockModernIndividualTable);
    }

    @After
    public void tearDown() {
        boxscoreItem = null;
        playByPlayItem = null;
        getItemRequest = null;
        actualBoxscore = null;
        actualPlayByPlayResponse = null;
        underTest = null;
    }

    @Test
    public void putBoxscore_validData_putSucceeds() throws IOException {
        setBoxscoreItem();
        whenPutBoxscoreIsCalled();
        verifyBoxscoreDdbPutBoxscore();
    }

    @Test
    public void putBoxscore_ddbException_exceptionThrown() throws IOException {
        setBoxscoreItem();
        expectPutBoxscoreThrowsException();
        assertThrows(DynamoDbException.class, () -> whenPutBoxscoreIsCalled());
    }

    @Test
    public void getBoxscore_validData_getSucceeds() throws IOException {
        setBoxscoreItem();
        setGetRequest();
        expectGetBoxscoreReturnsBoxscore();
        whenGetBoxscoreIsCalled();
        validateBoxscore();
    }

    @Test
    public void getBoxscore_ddbException_exceptionThrown() throws IOException {
        setBoxscoreItem();
        setGetRequest();
        expectGetBoxscoreThrowsException();
        assertThrows(DynamoDbException.class, () -> whenGetBoxscoreIsCalled());
    }


    @Test
    public void putPlayByPlay_validData_putSucceeds() throws IOException {
        setPlayByPlayItem();
        whenPutPlayByPlayIsCalled();
        verifyPlayByPlayDdbPutPlayByPlay();
    }

    @Test
    public void putPlayByPlay_ddbException_exceptionThrown() throws IOException {
        setPlayByPlayItem();
        expectPutPlayByPlayThrowsException();
        assertThrows(DynamoDbException.class, () -> whenPutPlayByPlayIsCalled());
    }

    @Test
    public void getPlayByPlay_validData_getSucceeds() throws IOException {
        setPlayByPlayItem();
        setGetRequest();
        expectGetPlayByPlayReturnsPlayByPlay();
        whenGetPlayByPlayIsCalled();
        validatePlayByPlayResponse();
    }

    @Test
    public void getPlayByPlay_ddbException_exceptionThrown() throws IOException {
        setPlayByPlayItem();
        setGetRequest();
        expectGetPlayByPlayThrowsException();
        assertThrows(DynamoDbException.class, () -> whenGetPlayByPlayIsCalled());
    }

    private void setBoxscoreItem() throws IOException {
        boxscoreItem = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreLiveGameResponse.json"),
                Boxscore.class);
    }

    private void setPlayByPlayItem() throws IOException {
        playByPlayItem = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/playByPlayOffGameResponse.json"),
                PlayByPlay.class);
    }

    private void verifyBoxscoreDdbPutBoxscore() {
        verify(mockBoxscoreTable, times(1)).putItem(boxscoreItem);
    }

    private void verifyPlayByPlayDdbPutPlayByPlay() {
        verify(mockPlayByPlayTable, times(1)).putItem(playByPlayItem);
    }

    private void expectGetBoxscoreReturnsBoxscore() {
        when(mockBoxscoreTable.getItem(getItemRequest)).thenReturn(boxscoreItem);
    }

    private void expectGetPlayByPlayReturnsPlayByPlay() {
        when(mockPlayByPlayTable.getItem(getItemRequest)).thenReturn(playByPlayItem);
    }

    private void expectPutBoxscoreThrowsException() {
        doThrow(DynamoDbException.builder()
                .build())
                .when(mockBoxscoreTable)
                .putItem(boxscoreItem);
    }

    private void expectPutPlayByPlayThrowsException() {
        doThrow(DynamoDbException.builder()
                .build())
                .when(mockPlayByPlayTable)
                .putItem(playByPlayItem);
    }

    private void expectGetBoxscoreThrowsException() {
        doThrow(DynamoDbException.builder()
                .build())
                .when(mockBoxscoreTable)
                .getItem(getItemRequest);
    }

    private void expectGetPlayByPlayThrowsException() {
        doThrow(DynamoDbException.builder()
                .build())
                .when(mockPlayByPlayTable)
                .getItem(getItemRequest);
    }

    private void setGetRequest() {
        getItemRequest = GetItemEnhancedRequest.builder()
                .key(Key.builder()
                        .partitionValue(gameId)
                        .build())
                .consistentRead(true)
                .build();
    }

    private void whenPutBoxscoreIsCalled() {
        underTest.putBoxscore(boxscoreItem);
    }

    private void whenPutPlayByPlayIsCalled() {
        underTest.putPlayByPlay(playByPlayItem);
    }

    private void whenGetBoxscoreIsCalled() {
        actualBoxscore = underTest.getBoxscore(gameId);
    }

    private void whenGetPlayByPlayIsCalled() {
        actualPlayByPlayResponse = underTest.getPlayByPlay(gameId);
    }

    private void validateBoxscore() {
        assertEquals(boxscoreItem, actualBoxscore);
    }

    private void validatePlayByPlayResponse() {
        assertEquals(playByPlayItem, actualPlayByPlayResponse);
    }
}