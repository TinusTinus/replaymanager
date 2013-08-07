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
import java.util.GregorianCalendar;

import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.model.AssistType;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Player;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link Game}.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class GameTest {
    /** Tests the constructor and a bunch of methods in the Game class. */
    @Test
    public void test() {
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus);

        Assert.assertEquals("DJ Alberto Lara (Hulk / Wolverine / Sentinel) vs. MvdR (Wolverine / Zero / Doctor Doom)",
                game.getDescription(false));
        Assert.assertEquals(
                "DJ Alberto Lara (Hulk (Gamma Wave) / Wolverine (Berserker Slash) / Sentinel (Sentinel Force Charge)) vs. MvdR (Wolverine (Berserker Barrage) / Zero (Ryuenjin) / Doctor Doom (Plasma Beam))",
                game.getDescription(true));
    }

    /** Tests the equals method. */
    @Test
    public void testNotEqual() {
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game1 = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus);

        Player badhyper = new Player("badhyper");
        Team teamBadhyper = new Team(Umvc3Character.FELICIA, Umvc3Character.MODOK, Umvc3Character.WESKER);
        Player nemo = new Player("Nemo");
        Team teamNemo = new Team(Umvc3Character.NOVA, Umvc3Character.SPENCER, Umvc3Character.DOCTOR_STRANGE);
        Game game2 = new Game(badhyper, teamBadhyper, nemo, teamNemo);

        Assert.assertFalse(game1.equals(game2));
        Assert.assertFalse(game2.equals(game1));
    }

    /** Tests the equals method. */
    @Test
    public void testEqualSame() {
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus);

        Assert.assertEquals(game, game);
    }

    /** Tests the equals method. */
    @Test
    public void testEquals() {
        Player djAlbertoLara1 = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara1 = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus1 = new Player("MvdR");
        Team teamTinus1 = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game1 = new Game(djAlbertoLara1, teamDjAlbertoLara1, tinus1, teamTinus1);

        Player djAlbertoLara2 = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara2 = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus2 = new Player("MvdR");
        Team teamTinus2 = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game2 = new Game(djAlbertoLara2, teamDjAlbertoLara2, tinus2, teamTinus2);

        Assert.assertEquals(game1, game2);
    }
    
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
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus);
        
        // marshal
        String string = mapper.writeValueAsString(game);

        Assert.assertNotNull(string);
        log.info("JSON: " + string);
        
        // unmarshal
        Game unmarshalled = mapper.readValue(string, Game.class);
        
        Assert.assertNotSame(game, unmarshalled);
        Assert.assertEquals(game, unmarshalled);
    }
    
    /** Tests the construction of the base filename. */
    @Test
    public void testGetBaseFilename() {
        Date date = new GregorianCalendar(2012, 0, 4, 18, 25, 46).getTime();
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus);
        
        String baseFilename = game.getBaseFilename(date);
        
        Assert.assertEquals("20120104182546-DJ_Alberto_Lara(Hulk-Wolvie-Sent)_vs_MvdR(Wolvie-Zero-Doom)", baseFilename);
    }
    
    /** Tests the construction of the base filename where the player names contain invalid symbols. */
    @Test
    public void testGetBaseFilenameSymbols() {
        Date date = new GregorianCalendar(2012, 0, 4, 18, 25, 46).getTime();
        Player playerOne = new Player("Pipe|Pipe");
        Team teamOne = new Team(Umvc3Character.RYU, Umvc3Character.AKUMA, Umvc3Character.C_VIPER);
        Player playerTwo = new Player("Star*Star");
        Team teamTwo = new Team(Umvc3Character.CAPTAIN_AMERICA, Umvc3Character.IRON_MAN, Umvc3Character.THOR);
        Game game = new Game(playerOne, teamOne, playerTwo, teamTwo);
        
        String baseFilename = game.getBaseFilename(date);
        
        Assert.assertEquals("20120104182546-Pipe_Pipe(Ryu-Akuma-Viper)_vs_Star_Star(Cap-IronMan-Thor)", baseFilename);
    }

    /** Tests the constructor's behaviour when passing in a null value. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullPlayerOne() {
        Team teamOne = new Team(Umvc3Character.HULK, Umvc3Character.WOLVERINE, Umvc3Character.SENTINEL);
        Player playerTwo = new Player("MvdR");
        Team teamTwo = new Team(Umvc3Character.WOLVERINE, Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM);
        new Game(null, teamOne, playerTwo, teamTwo);
    }
    
    /** Tests the constructor's behaviour when passing in a null value. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullPlayerTwo() {
        Player playerOne = new Player("DJ Alberto Lara");
        Team teamOne = new Team(Umvc3Character.HULK, Umvc3Character.WOLVERINE, Umvc3Character.SENTINEL);
        Team teamTwo = new Team(Umvc3Character.WOLVERINE, Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM);
        new Game(playerOne, teamOne, null, teamTwo);
    }
    
    /** Tests the constructor's behaviour when passing in a null value. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullTeamOne() {
        Player playerOne = new Player("DJ Alberto Lara");
        Player playerTwo = new Player("MvdR");
        Team teamTwo = new Team(Umvc3Character.WOLVERINE, Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM);
        new Game(playerOne, null, playerTwo, teamTwo);
    }
    
    /** Tests the constructor's behaviour when passing in a null value. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullTeamTwo() {
        Player playerOne = new Player("DJ Alberto Lara");
        Team teamOne = new Team(Umvc3Character.HULK, Umvc3Character.WOLVERINE, Umvc3Character.SENTINEL);
        Player playerTwo = new Player("MvdR");
        new Game(playerOne, teamOne, playerTwo, null);
    }
}
