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
import nl.mvdr.umvc3replayanalyser.ocr.OCREngine;
import nl.mvdr.umvc3replayanalyser.ocr.OCRException;

/**
 * Mock implementation of {@link OCREngine}.
 * 
 * @author Martijn van de Rijdt
 */
public class OCREngineMock implements OCREngine {
    /** {@inheritDoc} */
    @Override
    public String ocrLine(BufferedImage image) throws OCRException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    @Override
    public Umvc3Character ocrCharacter(BufferedImage image) throws OCRException {
        throw new UnsupportedOperationException();
    }

}
