package nl.tinus.umvc3replayanalyser.gui;

import javafx.scene.image.Image;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Umvc3ReplayManager.
 * 
 * @author Martijn van de Rijdt
 */
public class IconsTest {
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
