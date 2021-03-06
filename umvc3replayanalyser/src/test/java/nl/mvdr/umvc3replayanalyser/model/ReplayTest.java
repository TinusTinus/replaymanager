/*
 * Copyright 2012, 2013 Martijn van de Rijdt 
 * 
 * This file is part of the Ultimate Marvel vs Capcom 3 Replay Manager.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with the Ultimate Marvel vs Capcom 3
 * Replay Manager. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mvdr.umvc3replayanalyser.model;

import java.io.IOException;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link Replay}.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class ReplayTest {
    /** Tests the constructor's behaviour in case of null values for parameters. */
    @Test(expected = NullPointerException.class)
    public void testNullDate() {
        new Replay(null, createGame(), "video", "preview");
    }
    
    /** Tests the constructor's behaviour in case of null values for parameters. */
    @Test(expected = NullPointerException.class)
    public void testNullGame() {
        new Replay(new Date(), null, "video", "preview");
    }
    
    /** Tests the constructor's behaviour in case of null values for parameters. */
    @Test(expected = NullPointerException.class)
    public void testNullVideo() {
        new Replay(new Date(), createGame(), null, "preview");
    }
    
    /** Tests the constructor's behaviour in case of null values for parameters. */
    @Test(expected = NullPointerException.class)
    public void testNullImage() {
        new Replay(new Date(), createGame(), "video", null);
    }
    
    /** 
     * Tests converting a value to a JSON string and back. 
     * 
     * @throws IOException unexpected
     */
    @Test
    public void testJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Replay replay = new Replay(new Date(), createGame(), "video", "preview");
        
        // marshal
        String string = mapper.writeValueAsString(replay);

        Assert.assertNotNull(string);
        log.info("JSON: " + string);
        
        // unmarshal
        Replay unmarshalled = mapper.readValue(string, Replay.class);
        
        Assert.assertNotSame(replay, unmarshalled);
        Assert.assertEquals(replay, unmarshalled);
    }

    /**
     * Constructs a game.
     * 
     * @return game
     */
    private Game createGame() {
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus);
        return game;
    }
}
