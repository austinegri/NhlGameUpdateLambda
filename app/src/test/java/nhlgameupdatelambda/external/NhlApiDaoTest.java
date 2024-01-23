package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import nhlgameupdatelambda.data.BoxscoreResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static nhlgameupdatelambda.external.NhlApiDao.PLAY_BY_PLAY_ENDPOINT;


public class NhlApiDaoTest {


    private String gameId;
    private NhlApiDao underTest;
    private BoxscoreResponse expectedBoxscoreResponse;
    private BoxscoreResponse actualBoxscoreResponse;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        underTest = new NhlApiDao();
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
        whenGetBoxscoreIsCalled();
        assertEquals(expectedBoxscoreResponse, actualBoxscoreResponse);
    }


    private void setupExpectedInProgressBoxscoreResponse() throws IOException {
        expectedBoxscoreResponse = objectMapper.readValue(new File("src/test/java/nhlgameupdatelambda/testData/boxscoreFutGameResponse.json"),
                BoxscoreResponse.class);
    }
    private void whenGetBoxscoreIsCalled() {
        actualBoxscoreResponse = underTest.getBoxscore(gameId);
    }
    private void setupInProgressGameId() {
        gameId = "2023020698";
    }
}