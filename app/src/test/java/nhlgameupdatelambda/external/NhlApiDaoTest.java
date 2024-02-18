package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
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
    private BoxscoreResponse expectedBoxscoreResponse;
    private BoxscoreResponse actualBoxscoreResponse;

    @Mock
    private ObjectMapper mockObjectMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        setupExpectedInProgressBoxscoreResponse();
        expectObjectMapperOnInProgressBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyAll();
    }

    @Test
    public void getBoxscore_critGameId_boxscoreDataReturned() throws IOException {
        setupCritGameId();
        setupExpectedCritBoxscoreResponse();
        expectObjectMapperOnCritBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyAll();
    }

    @Test
    public void getBoxscore_finalGameId_boxscoreDataReturned() throws IOException {
        setupFinalGameId();
        setupExpectedFinalBoxscoreResponse();
        expectObjectMapperOnFinalBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyAll();
    }

    @Test
    public void getBoxscore_futGameId_boxscoreDataReturned() throws IOException {
        setupFutureGameId();
        setupExpectedFutBoxscoreResponse();
        expectObjectMapperOnFutBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyAll();
    }

    @Test
    public void getBoxscore_offGameId_boxscoreDataReturned() throws IOException {
        setupOffGameId();
        setupExpectedOffBoxscoreResponse();
        expectObjectMapperOnOffBoxscoreUrl();
        whenGetBoxscoreIsCalled();
        verifyAll();
    }

    private void verifyAll() {
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

    private void expectObjectMapperOnOffBoxscoreUrl() throws IOException {
        when(mockObjectMapper.readValue(any(InputStream.class), eq(BoxscoreResponse.class)))
                .thenReturn(objectMapper.readValue(
                        new File("src/test/java/nhlgameupdatelambda/testData/boxscoreOffGameResponse.json"),
                        BoxscoreResponse.class));
    }

    private void whenGetBoxscoreIsCalled() {
        actualBoxscoreResponse = underTest.getBoxscore(gameId);
    }

    private void setupCritGameId() {
        gameId = "2023020861";
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