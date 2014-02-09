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
package nl.mvdr.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;

import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.ocr.OCRException;

/**
 * Analyses an image of an Ultimate Marvel vs Capcom 3 versus screen, using optical character recognition (OCR) to
 * determine player and character names.
 * 
 * @author Martijn van de Rijdt
 */
@FunctionalInterface
public interface VersusScreenAnalyser {
    /**
     * Analyses the given image and returns the Game represented by this image.
     * 
     * @param versusImage
     *            image to be analysed
     * @return game represented by this image; all fields are filled except winningSide and assists
     * @throws OCRException
     *             in case image analysis fails
     */
    Game analyse(BufferedImage versusImage) throws OCRException;

    /**
     * Checks if the given image is a candidate to be a versus screen.
     * 
     * Implementations of this method should be a lot cheaper than {@link #analyse(BufferedImage)}.
     * 
     * @param image
     *            image to be checked
     * @return true if the image could be a versus screen, false if it definitely is not
     */
    default boolean canBeVersusScreen(BufferedImage image) {
        return true;
    }
}