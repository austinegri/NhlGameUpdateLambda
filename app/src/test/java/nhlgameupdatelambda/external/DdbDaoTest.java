package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.testHelpers.TestLogger;
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
    private BoxscoreResponse boxscoreItem;
    private GetItemEnhancedRequest getItemRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DynamoDbTable<BoxscoreResponse> mockBoxscoreTable;

    private DdbDao underTest;
    private BoxscoreResponse actualBoxscoreResponse;

    @Before
    public void setUp() {
        gameId = 2023020900;
        underTest = new DdbDao(new TestLogger(), mockBoxscoreTable);
    }

    @After
    public void tearDown() {
        boxscoreItem = null;
        getItemRequest = null;
        actualBoxscoreResponse = null;
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
        setGetBoxscoreRequest();
        expectGetBoxscoreReturnsBoxscore();
        whenGetBoxscoreIsCalled();
        validateBoxscoreResponse();
    }

    @Test
    public void getBoxscore_ddbException_exceptionThrown() throws IOException {
        setBoxscoreItem();
        setGetBoxscoreRequest();
        expectGetBoxscoreThrowsException();
        assertThrows(DynamoDbException.class, () -> whenGetBoxscoreIsCalled());
    }

    private void setBoxscoreItem() throws IOException {
        boxscoreItem = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreLiveGameResponse.json"),
                BoxscoreResponse.class);
    }

    private void verifyBoxscoreDdbPutBoxscore() {
        verify(mockBoxscoreTable, times(1)).putItem(boxscoreItem);
    }

    private void expectGetBoxscoreReturnsBoxscore() {
        when(mockBoxscoreTable.getItem(getItemRequest)).thenReturn(boxscoreItem);
    }

    private void expectPutBoxscoreThrowsException() {
        doThrow(DynamoDbException.builder()
                .build())
                .when(mockBoxscoreTable)
                .putItem(boxscoreItem);
    }

    private void expectGetBoxscoreThrowsException() {
        doThrow(DynamoDbException.builder()
                .build())
                .when(mockBoxscoreTable)
                .getItem(getItemRequest);
    }

    private void setGetBoxscoreRequest() {
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

    private void whenGetBoxscoreIsCalled() {
        actualBoxscoreResponse = underTest.getBoxscore(gameId);
    }

    private void validateBoxscoreResponse() {
        assertEquals(boxscoreItem, actualBoxscoreResponse);
    }
}