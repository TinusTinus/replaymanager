package nl.tinus.umvc3replayanalyser.model.predicate;

import java.util.Date;

import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.model.Side;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for GamertagPrefixReplayPredicate.
 * 
 * @author Martijn van de Rijdt
 */
public class GamertagPrefixReplayPredicateTest {
    /** Tests the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullPrefix() {
        new GamertagPrefixReplayPredicate(null, null);
    }

    /** Tests the apply method. */
    @Test
    public void testMatchAnyPlayerOne() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test1", null);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchAnyPlayerTwo() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test2", null);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchAnyPlayerBoth() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test", null);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchAnyPlayerNeither() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("nomatch", null);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneMatch() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test1", Side.PLAYER_ONE);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneOther() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test2", Side.PLAYER_ONE);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneNeither() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("nomatch", Side.PLAYER_ONE);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerOneBoth() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test", Side.PLAYER_ONE);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoMatch() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test2", Side.PLAYER_TWO);
        Assert.assertTrue(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoOther() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test1", Side.PLAYER_TWO);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoNeither() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("nomatch", Side.PLAYER_TWO);
        Assert.assertFalse(predicate.apply(replay));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchPlayerTwoBoth() {
        Replay replay = createReplay("test1", "test2");
        GamertagPrefixReplayPredicate predicate = new GamertagPrefixReplayPredicate("test", Side.PLAYER_TWO);
        Assert.assertTrue(predicate.apply(replay));
    }

    /**
     * Creates a replay where the players have the given gamertags. Other fields are filled with mock values, since
     * those are not the focus in these tests.
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
