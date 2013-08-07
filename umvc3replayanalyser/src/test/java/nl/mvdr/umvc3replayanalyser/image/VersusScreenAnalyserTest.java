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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.mvdr.umvc3replayanalyser.ocr.OCREngineMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link VersusScreenAnalyserImpl}.
 * 
 * @author Martijn van de Rijdt
 */
public class VersusScreenAnalyserTest {
    /** Object to be tested. */
    private VersusScreenAnalyserImpl analyser;
    
    /** Setup method. */
    @Before
    public void setUp() {
        this.analyser = new VersusScreenAnalyserImpl(new OCREngineMock());
    }
    
    
    /**
     * Tests the {@link VersusScreenAnalyserImpl#canBeVersusScreen(BufferedImage)} method with an image that actually is a versus screen.
     * 
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testCanBeVersusScreenTrue() throws IOException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vs.png"));
        Assert.assertTrue(this.analyser.canBeVersusScreen(image));
    }

    /**
     * Tests the {@link VersusScreenAnalyserImpl#canBeVersusScreen(BufferedImage)} method with an image that cannot be a versus screen.
     * 
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testCanBeVersusScreenFalse() throws IOException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vsinverted.png"));
        Assert.assertFalse(this.analyser.canBeVersusScreen(image));
    }
}
