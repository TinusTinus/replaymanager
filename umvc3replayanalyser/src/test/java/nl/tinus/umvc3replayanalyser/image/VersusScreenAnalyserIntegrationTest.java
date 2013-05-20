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
package nl.tinus.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tinus.umvc3replayanalyser.config.Configuration;
import nl.tinus.umvc3replayanalyser.config.PropertiesConfiguration;
import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.ocr.OCRException;
import nl.tinus.umvc3replayanalyser.ocr.TesseractOCREngine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for VersusScreenAnalyser. Uses an actual OCR engine implementation, so this test is more of an integration
 * test than a unit test.
 * 
 * @author Martijn van de Rijdt
 */
public class VersusScreenAnalyserIntegrationTest {
    /** Analyser to be tested. */
    private VersusScreenAnalyserImpl analyser;

    /** Setup method. */
    @Before
    public void setUp() {
        Configuration configuration = new PropertiesConfiguration();
        TesseractOCREngine ocrEngine = new TesseractOCREngine(configuration);
        this.analyser = new VersusScreenAnalyserImpl(ocrEngine);
    }

    /**
     * Test case with an image that is too small.
     * 
     * @throws IOException
     *             unexpected
     * @throws OCRException
     *             unexpected
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAnalyseSmallImage() throws IOException, OCRException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/MvdR.png"));
        this.analyser.analyse(image);
    }

    /**
     * Test case with an image that contains no text.
     * 
     * @throws IOException
     *             unexpected
     * @throws OCRException
     *             expected, since the image contains no text
     */
    @Test(expected = OCRException.class)
    public void testAnalyseEmptyImage() throws IOException, OCRException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/empty.png"));
        this.analyser.analyse(image);
    }

    /**
     * Test case with an actual versus screen.
     * 
     * @throws IOException
     *             unexpected
     * @throws OCRException
     *             unexpected
     */
    @Test
    public void testAnalyse() throws IOException, OCRException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vs.png"));
        Game game = this.analyser.analyse(image);

        Assert.assertNotNull(game);

        // check player names
        Assert.assertEquals("MvdR", game.getPlayerOne().getGamertag());
        Assert.assertEquals("mistermkl", game.getPlayerTwo().getGamertag());

        // check characters
        Assert.assertEquals(Umvc3Character.WOLVERINE, game.getTeamOne().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.ZERO, game.getTeamOne().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, game.getTeamOne().getThirdCharacter());

        Assert.assertEquals(Umvc3Character.MORRIGAN, game.getTeamTwo().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.HAGGAR, game.getTeamTwo().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.SHUMA_GORATH, game.getTeamTwo().getThirdCharacter());

        // check assists
        Assert.assertEquals(AssistType.GAMMA, game.getTeamOne().getFirstAssistType());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamOne().getSecondAssistType());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamOne().getThirdAssistType());

        Assert.assertEquals(AssistType.GAMMA, game.getTeamTwo().getFirstAssistType());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamTwo().getSecondAssistType());
    }

    /**
     * Test case with an actual versus screen.
     * 
     * @throws IOException
     *             unexpected
     * @throws OCRException
     *             unexpected
     */
    @Test
    public void testAnalyseWithGivenGamertags() throws IOException, OCRException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vs.png"));
        Game game = this.analyser.analyse(image, "MvdR", "Training Dummy");

        Assert.assertNotNull(game);

        // check player names
        Assert.assertEquals("MvdR", game.getPlayerOne().getGamertag());
        Assert.assertEquals("Training Dummy", game.getPlayerTwo().getGamertag());

        // check characters
        Assert.assertEquals(Umvc3Character.WOLVERINE, game.getTeamOne().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.ZERO, game.getTeamOne().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, game.getTeamOne().getThirdCharacter());

        Assert.assertEquals(Umvc3Character.MORRIGAN, game.getTeamTwo().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.HAGGAR, game.getTeamTwo().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.SHUMA_GORATH, game.getTeamTwo().getThirdCharacter());

        // check assists
        Assert.assertEquals(AssistType.GAMMA, game.getTeamOne().getFirstAssistType());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamOne().getSecondAssistType());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamOne().getThirdAssistType());

        Assert.assertEquals(AssistType.GAMMA, game.getTeamTwo().getFirstAssistType());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamTwo().getSecondAssistType());
    }

    /**
     * Test case with an actual versus screen.
     * 
     * @throws IOException
     *             unexpected
     * @throws OCRException
     *             unexpected
     */
    @Test
    public void testAnalyseWithoutGamertags() throws IOException, OCRException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vswithoutnames.png"));
        Game game = this.analyser.analyse(image, "MvdR", "Training Dummy");

        Assert.assertNotNull(game);

        // check player names
        Assert.assertEquals("MvdR", game.getPlayerOne().getGamertag());
        Assert.assertEquals("Training Dummy", game.getPlayerTwo().getGamertag());

        System.out.println(game.getDescription(true, false));

        // check characters
        Assert.assertEquals(Umvc3Character.STRIDER_HIRYU, game.getTeamOne().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.NEMESIS, game.getTeamOne().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.VIEWTIFUL_JOE, game.getTeamOne().getThirdCharacter());

        Assert.assertEquals(Umvc3Character.SUPER_SKRULL, game.getTeamTwo().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.ROCKET_RACCOON, game.getTeamTwo().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.DOCTOR_STRANGE, game.getTeamTwo().getThirdCharacter());

        // check assists
        Assert.assertEquals(AssistType.GAMMA, game.getTeamOne().getFirstAssistType());

        // Joe's assist type is not checked, because it is covered up by the lightning bolt in this specific screenshot.
        // It happens, this is exactly why the assist type is optional.

        Assert.assertEquals(AssistType.ALPHA, game.getTeamTwo().getThirdAssistType());
    }
}
