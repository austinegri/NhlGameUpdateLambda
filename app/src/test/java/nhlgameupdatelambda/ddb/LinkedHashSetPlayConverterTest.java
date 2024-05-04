package nhlgameupdatelambda.ddb;

import com.google.common.collect.ImmutableList;
import nhlgameupdatelambda.data.playbyplay.Play;
import nhlgameupdatelambda.data.playbyplay.PlayType;
import nhlgameupdatelambda.data.playbyplay.playdetail.PlayDetail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class LinkedHashSetPlayConverterTest {
    private static final PlayDetail SHOT_DETAIL = PlayDetail.builder()
            .shotType("wrist")
            .shootingPlayerId(111)
            .goalieInNetId(222)
            .build();
    private static final Play SHOT_ON_GOAL = Play.builder()
            .typeDescKey(PlayType.SHOT_ON_GOAL)
            .details(SHOT_DETAIL)
            .build();
    private static final Play STOPPAGE = Play.builder()
            .typeDescKey(PlayType.STOPPAGE)
            .build();
    private static final LinkedHashSet<Play> PLAYS = ImmutableList.of(SHOT_ON_GOAL, STOPPAGE)
            .stream()
            .collect(Collectors.toCollection(LinkedHashSet::new));

    private LinkedHashSet<Play> expectedPlays;
    private LinkedHashSet<Play> actualPlays;
    private AttributeValue expectedAttributeValue;
    private AttributeValue actualAttributeValue;

    private LinkedHashSetPlayConverter underTest;

    @Before
    public void setUp() {
        underTest = new LinkedHashSetPlayConverter();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void transformFrom_playsToAttributeValue_convertedSuccessfully() {
        actualAttributeValue = underTest.transformFrom(PLAYS);
        assertNotNull(actualAttributeValue);
    }

    @Test
    public void transformTo_attributeValueToPlays_convertedSuccessfully() {
        AttributeValue converted = underTest.transformFrom(PLAYS);
        actualPlays = underTest.transformTo(converted);
        assertEquals(PLAYS, actualPlays);
    }

}