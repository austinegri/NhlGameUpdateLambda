package nhlgameupdatelambda.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.playbyplay.Play;
import nhlgameupdatelambda.data.playbyplay.PlayType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PlayDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(MockitoJUnitRunner.class)
public class DataMapperTest {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testPlayByPlayMapper() throws JsonProcessingException {
        final PlayDetail playDetail = PlayDetail.builder()
                .reason("Dummy reason")
                .build();
        final Play play = Play.builder()
                .typeDescKey(PlayType.STOPPAGE)
                .details(playDetail)
                .build();
        final String serializedPlay = OBJECT_MAPPER.writeValueAsString(play);
        final Play deserializedPlay = OBJECT_MAPPER.readValue(serializedPlay, Play.class);
        assertThat(serializedPlay, containsString("reason"));
        assertThat(serializedPlay, not(containsString("shootingPlayerId")));
    }

    @Test
    public void testPlayByPlayShotMapper() throws JsonProcessingException {
        final PlayDetail playDetail = PlayDetail.builder()
                .shootingPlayerId(1111)
                .build();
        final Play play = Play.builder()
                .typeDescKey(PlayType.SHOT_ON_GOAL)
                .details(playDetail)
                .build();
        final String serializedPlay = OBJECT_MAPPER.writeValueAsString(play);
        final Play deserializedPlay = OBJECT_MAPPER.readValue(serializedPlay, Play.class);
        assertThat(serializedPlay, containsString("shot-on-goal"));
        assertThat(serializedPlay, (containsString("shootingPlayerId")));
    }
}
