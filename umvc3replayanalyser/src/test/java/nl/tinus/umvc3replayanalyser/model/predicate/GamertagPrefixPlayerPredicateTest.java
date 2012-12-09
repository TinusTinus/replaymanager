package nl.tinus.umvc3replayanalyser.model.predicate;

import java.util.Arrays;
import java.util.List;

import nl.tinus.umvc3replayanalyser.model.Player;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * Test class for GamertagPrefixPlayerPredicate.
 * 
 * @author Martijn van de Rijdt
 */
public class GamertagPrefixPlayerPredicateTest {
    /** Tests the apply method. */
    @Test
    public void testExactMatch() {
        Player player = new Player("test");
        GamertagPrefixPlayerPredicate predicate = new GamertagPrefixPlayerPredicate("test");
        Assert.assertTrue(predicate.apply(player));
    }

    /** Tests the apply method. */
    @Test
    public void testPrefixMatch() {
        Player player = new Player("test");
        GamertagPrefixPlayerPredicate predicate = new GamertagPrefixPlayerPredicate("te");
        Assert.assertTrue(predicate.apply(player));
    }

    /** Tests the apply method. */
    @Test
    public void testNoMatch() {
        Player player = new Player("test");
        GamertagPrefixPlayerPredicate predicate = new GamertagPrefixPlayerPredicate("derp");
        Assert.assertFalse(predicate.apply(player));
    }

    /** Tests the apply method. */
    @Test
    public void testLongerName() {
        Player player = new Player("test");
        GamertagPrefixPlayerPredicate predicate = new GamertagPrefixPlayerPredicate("testing");
        Assert.assertFalse(predicate.apply(player));
    }

    /** Tests the constructor with a null argument. */
    @Test(expected = NullPointerException.class)
    public void testNull() {
        new GamertagPrefixPlayerPredicate(null);
    }

    /** Tests filtering a list using the predicate. */
    @Test
    public void testFilter() {
        Player test = new Player("test");
        Player testing = new Player("testing");
        Player someoneelse = new Player("someoneelse");
        Player tester = new Player("tester");
        Player te = new Player("te");
        List<Player> players = Arrays.asList(test, testing, someoneelse, tester, te);
        
        GamertagPrefixPlayerPredicate predicate = new GamertagPrefixPlayerPredicate("test");
        List<Player> filtered = ImmutableList.copyOf(Iterables.filter(players, predicate));
        
        List<Player> expected = Arrays.asList(test, testing, tester);
        
        Assert.assertEquals(expected, filtered);
    }

}
