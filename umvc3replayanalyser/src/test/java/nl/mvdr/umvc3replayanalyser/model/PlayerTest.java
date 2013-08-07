/*
 * Copyright 2013 Martijn van de Rijdt
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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link Player}.
 * 
 * @author Martijn van de Rijdt
 */
public class PlayerTest {
    /** Tests the constructor's null check. */
    @Test(expected = NullPointerException.class)
    public void testNullValue() {
        new Player(null);
    }
    
    /** Tests the constructor and the getter. */
    @Test
    public void test() {
        String name = "name";
        Player player = new Player(name);
        Assert.assertEquals(name, player.getGamertag());
    }
}
