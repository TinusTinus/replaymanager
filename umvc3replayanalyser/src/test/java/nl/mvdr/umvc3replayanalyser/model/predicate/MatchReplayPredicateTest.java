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
package nl.mvdr.umvc3replayanalyser.model.predicate;

import java.util.Date;

import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Player;
import nl.mvdr.umvc3replayanalyser.model.Replay;
import nl.mvdr.umvc3replayanalyser.model.Side;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;
import nl.mvdr.umvc3replayanalyser.model.predicate.MatchReplayPredicate;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MatchReplayPredicate}.
 * 
 * @author Martijn van de Rijdt
 */
public class MatchReplayPredicateTest {
    /** Tests the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullPrefix() {
        new MatchReplayPredicate(null, null, null, null, null, null, null, false, Side.PLAYER_ONE);
    }

    /** Tests the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullSide() {
        new MatchReplayPredicate("prefix", null, null, null, null, null, null, false, null);
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneGamertagMatch() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("test1", null, null, null, null, null, null, false,
                Side.PLAYER_ONE);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneGamertagOther() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("test2", null, null, null, null, null, null, false,
                Side.PLAYER_ONE);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneGamertagNeither() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("nomatch", null, null, null, null, null, null, false,
                Side.PLAYER_ONE);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneGamertagBoth() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("test", null, null, null, null, null, null, false,
                Side.PLAYER_ONE);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoGamertagMatch() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("test2", null, null, null, null, null, null, false,
                Side.PLAYER_TWO);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoGamertagOther() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("test1", null, null, null, null, null, null, false,
                Side.PLAYER_TWO);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoGamertagNeither() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("nomatch", null, null, null, null, null, null, false,
                Side.PLAYER_TWO);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoGamertagBoth() {
        Replay replay = createReplay("test1", "test2");
        MatchReplayPredicate predicate = new MatchReplayPredicate("test", null, null, null, null, null, null, false,
                Side.PLAYER_TWO);
        Assert.assertTrue(predicate.apply(replay));
    }
    
    /**
     * Creates a replay where the players have the given gamertags. Other fields are filled with mock values.
     * 
     * @param gamertagPlayerOne
     *            gamertag for player one
     * @param gamertagPlayerTwo
     *            gamertag for player two
     * @return replay
     */
    private Replay createReplay(String gamertagPlayerOne, String gamertagPlayerTwo) {
        Player playerOne = new Player(gamertagPlayerOne);
        Player playerTwo = new Player(gamertagPlayerTwo);
        Team teamOne = new Team(Umvc3Character.WOLVERINE, Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM);
        Team teamTwo = new Team(Umvc3Character.NOVA, Umvc3Character.SPENCER, Umvc3Character.DOCTOR_STRANGE);
        Game game = new Game(playerOne, teamOne, playerTwo, teamTwo);
        Replay replay = new Replay(new Date(), game, "vid", "image");
        return replay;
    }
}
