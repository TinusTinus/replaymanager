package nl.tinus.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
    private VersusScreenAnalyser analyser;

    /** Setup method. */
    @Before
    public void setUp() {
        TesseractOCREngine ocrEngine = new TesseractOCREngine();
        this.analyser = new VersusScreenAnalyser(ocrEngine);
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
        Assert.assertEquals(AssistType.GAMMA, game.getTeamOne().getFirstAssist());
        Assert.assertEquals(Umvc3Character.ZERO, game.getTeamOne().getSecondCharacter());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamOne().getSecondAssist());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, game.getTeamOne().getThirdCharacter());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamOne().getThirdAssist());

        Assert.assertEquals(Umvc3Character.MORRIGAN, game.getTeamTwo().getFirstCharacter());
        Assert.assertEquals(AssistType.GAMMA, game.getTeamTwo().getFirstAssist());
        Assert.assertEquals(Umvc3Character.HAGGAR, game.getTeamTwo().getSecondCharacter());
        Assert.assertEquals(AssistType.ALPHA, game.getTeamTwo().getSecondAssist());
        Assert.assertEquals(Umvc3Character.SHUMA_GORATH, game.getTeamTwo().getThirdCharacter());
        // TODO Shuma covers up the pixel we want to inspect; uncomment once there are character-specific exceptions
        // Assert.assertEquals(AssistType.ALPHA, game.getTeamTwo().getThirdAssist());
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

        // check characters
        Assert.assertEquals(Umvc3Character.STRIDER_HIRYU, game.getTeamOne().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.NEMESIS, game.getTeamOne().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.VIEWTIFUL_JOE, game.getTeamOne().getThirdCharacter());

        Assert.assertEquals(Umvc3Character.SUPER_SKRULL, game.getTeamTwo().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.ROCKET_RACCOON, game.getTeamTwo().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.DOCTOR_STRANGE, game.getTeamTwo().getThirdCharacter());
    }
}
