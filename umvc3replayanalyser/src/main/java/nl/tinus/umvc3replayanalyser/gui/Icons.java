package nl.tinus.umvc3replayanalyser.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.image.Image;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

/**
 * Wrapper for loading and caching icons.
 * 
 * @author Martijn van de Rijdt
 */
class Icons {
    /** Singleton instance. */
    private static Icons INSTANCE = new Icons();

    /** Map. */
    private final Map<Umvc3Character, Image> portraits;
    /** Map. */
    private final Map<Umvc3Character, Image> icons;
    /** Random. */
    private final Random random;

    /** Private constructor since this is a singleton class. */
    private Icons() {
        super();
        this.portraits = new HashMap<>(Umvc3Character.values().length);
        this.icons = new HashMap<>(Umvc3Character.values().length);
        this.random = new Random();
    }

    /**
     * Singleton instance getter.
     * 
     * @return singleton instance
     */
    static Icons get() {
        return INSTANCE;
    }

    /**
     * Retrieves an icon from the map, or, if it does not occur in the map yet, loads and adds it to the map.
     * 
     * @param cache
     *            cache
     * @param key
     *            key
     * @param prefix
     *            prefix to be used in the url when loading the image
     * @return image
     */
    private Image getCached(Map<Umvc3Character, Image> cache, Umvc3Character key, String prefix) {
        Image result = cache.get(key);
        if (result == null) {
            String url = prefix + key.getShortName() + ".png";
            try {
                result = new Image(url);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Image cannot be loaded: " + url, e);
            }
            portraits.put(key, result);
        }
        return result;
    }

    /**
     * Retrieves the given character's portrait.
     * 
     * @param character
     *            character
     * @return portrait image
     */
    Image getPortrait(Umvc3Character character) {
        return getCached(portraits, character, "portrait-");
    }

    /**
     * Retrieves the given character's icon.
     * 
     * @param character
     *            character
     * @return icon image
     */
    Image getIcon(Umvc3Character character) {
        return getCached(icons, character, "icon-");
    }
    
    /**
     * Randomly returns a character's portrait.
     * 
     * @return protrait
     */
    Image getRandomPortrait() {
        // Select a random character and use that character's portrait as the icon.
        int characterIndex = random.nextInt(Umvc3Character.values().length);
        Umvc3Character character = Umvc3Character.values()[characterIndex];
        return getPortrait(character);
    }
}
