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
package nl.mvdr.umvc3replayanalyser.gui;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Icons}.
 * 
 * @author Martijn van de Rijdt
 */
public class IconsTest {
    /** Setup method. */
    @Before
    public void setUp() {
     // Force JavaFX graphics initialisation; this is required by the Image constructor.
        // When running the actual application this is performed by Application.launch().
        new JFXPanel();
    }
    
    /** Tests that all the character portraits are available and can be loaded. */
    @Test
    public void testPortraits() {
        for (Umvc3Character character: Umvc3Character.values()) {
            Image image = Icons.get().getPortrait(character);
            Assert.assertNotNull(image);
        }
    }
    
    /** Tests that all the character icons are available and can be loaded. */
    @Test
    public void testIcons() {
        for (Umvc3Character character: Umvc3Character.values()) {
            Image image = Icons.get().getIcon(character);
            Assert.assertNotNull(image);
        }
    }
}
