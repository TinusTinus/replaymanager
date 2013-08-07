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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.image.Image;
import lombok.Getter;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

/**
 * Wrapper for loading and caching icons.
 * 
 * @author Martijn van de Rijdt
 */
public class Icons {
    /** Singleton instance. */
    private static Icons INSTANCE = new Icons();

    // TODO the She-Hulk portrait is cut off, replace it
    
    /** Map. */
    private final Map<Umvc3Character, Image> portraits;
    /** Map. */
    private final Map<Umvc3Character, Image> icons;
    /** Icon of Ultimate Marvel vs Capcom 3. */
    @Getter
    private final Image gameIcon;
    /** Random. */
    private final Random random;

    /** Private constructor since this is a singleton class. */
    private Icons() {
        super();
        this.portraits = new HashMap<>(Umvc3Character.values().length);
        this.icons = new HashMap<>(Umvc3Character.values().length);
        this.random = new Random();
        this.gameIcon = new Image("icon-umvc3.png");
    }

    /**
     * Singleton instance getter.
     * 
     * @return singleton instance
     */
    public static Icons get() {
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
    public Image getPortrait(Umvc3Character character) {
        return getCached(portraits, character, "portrait-");
    }

    /**
     * Retrieves the given character's icon.
     * 
     * @param character
     *            character
     * @return icon image
     */
    public Image getIcon(Umvc3Character character) {
        return getCached(icons, character, "icon-");
    }
    
    /**
     * Randomly returns a character's portrait.
     * 
     * @return protrait
     */
    public Image getRandomPortrait() {
        // Select a random character and use that character's portrait as the icon.
        int characterIndex = random.nextInt(Umvc3Character.values().length);
        Umvc3Character character = Umvc3Character.values()[characterIndex];
        return getPortrait(character);
    }
}