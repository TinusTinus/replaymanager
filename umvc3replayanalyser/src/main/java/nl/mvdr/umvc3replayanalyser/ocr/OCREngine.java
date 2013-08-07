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
package nl.mvdr.umvc3replayanalyser.ocr;

import java.awt.image.BufferedImage;

import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

/**
 * Engine for optical character recognition (OCR), that is, recognising text from an image.
 * 
 * @author Martijn van de Rijdt
 */
public interface OCREngine {

    /**
     * Recognises the text in the given image. The image is expected to contain a single line.
     * 
     * @param image
     *            image to be analysed
     * @return text
     * @throws OCRException
     *             in case optical character recognition fails
     */
    String ocrLine(BufferedImage image) throws OCRException;

    /**
     * Recognises the character name in the given image. The image is expected to contain a single UMvC3 character name.
     * 
     * @param image
     *            image to be analysed
     * @return the character whose name is in the image
     * @throws OCRException
     *             in case optical character recongnition fails, or the recognised text cannot be matched to a character
     *             name
     */
    Umvc3Character ocrCharacter(BufferedImage image) throws OCRException;
}
