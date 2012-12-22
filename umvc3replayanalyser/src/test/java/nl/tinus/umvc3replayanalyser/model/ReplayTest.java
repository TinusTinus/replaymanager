package nl.tinus.umvc3replayanalyser.model;

import java.io.IOException;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Replay.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class ReplayTest {
    /** 
     * Tests converting a value to a JSON string and back. 
     * 
     * @throws IOException unexpected
     */
    @Test
    public void testJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus, Side.PLAYER_ONE);
        Replay replay = new Replay(new Date(), game, "video", "preview");
        
        // marshal
        String string = mapper.writeValueAsString(replay);

        Assert.assertNotNull(string);
        log.info("JSON: " + string);
        
        // unmarshal
        Replay unmarshalled = mapper.readValue(string, Replay.class);
        
        Assert.assertNotSame(replay, unmarshalled);
        Assert.assertEquals(replay, unmarshalled);
    }
}
