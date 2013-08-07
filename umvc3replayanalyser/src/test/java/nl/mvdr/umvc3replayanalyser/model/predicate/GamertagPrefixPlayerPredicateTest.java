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

import java.util.Arrays;
import java.util.List;

import nl.mvdr.umvc3replayanalyser.model.Player;
import nl.mvdr.umvc3replayanalyser.model.predicate.GamertagPrefixPlayerPredicate;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * Test class for {@link GamertagPrefixPlayerPredicate}.
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
    public void testPrefixCase() {
        Player player = new Player("test");
        GamertagPrefixPlayerPredicate predicate = new GamertagPrefixPlayerPredicate("TE");
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
