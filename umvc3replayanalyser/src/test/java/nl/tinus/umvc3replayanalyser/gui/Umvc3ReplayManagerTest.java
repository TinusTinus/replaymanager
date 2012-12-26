package nl.tinus.umvc3replayanalyser.gui;

import javafx.scene.image.Image;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Umvc3ReplayManager.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3ReplayManagerTest {
    /** Tests that all the character portraits are available and can be loaded. */
    @Test
    public void testPortraits() {
        for (Umvc3Character character: Umvc3Character.values()) {
            String fileUrl = "portrait-" + character.getShortName() + ".png";
            testImage(fileUrl);
        }
    }
    
    /** Tests that all the character icons are available and can be loaded. */
    @Test
    public void testIcons() {
        for (Umvc3Character character: Umvc3Character.values()) {
            String fileUrl = "icon-" + character.getShortName() + ".png";
            testImage(fileUrl);
        }
    }
    
    /** Tests if the given file url can be loaded as an image. */
    private void testImage(String fileUrl) {
        log.debug("Attempting to load " + fileUrl);
        Image image = new Image(fileUrl);
        Assert.assertNotNull(image);
    }
}
