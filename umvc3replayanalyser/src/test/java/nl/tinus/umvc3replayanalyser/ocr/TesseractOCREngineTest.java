package nl.tinus.umvc3replayanalyser.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for TesseractOCREngine.
 * 
 * @author Martijn van de Rijdt
 */
public class TesseractOCREngineTest {

    /**
     * Attempts to read a file.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrLineMvdR() throws OCRException, IOException {
        testOcrLine("src/test/resources/MvdR.png", "MvdR");
    }

    /**
     * Attempts to read a file.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrLineMistermkl() throws OCRException, IOException {
        testOcrLine("src/test/resources/mistermkl.png", "mistermkl");
    }

    /**
     * Attempts to read a file.
     * 
     * @param filename
     *            file to be recognised
     * @param expectedText
     *            text in the file
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    private void testOcrLine(String filename, String expectedText) throws OCRException, IOException {
        TesseractOCREngine engine = new TesseractOCREngine();

        BufferedImage image = ImageIO.read(new File(filename));

        String result = engine.ocrLine(image);

        Assert.assertEquals(expectedText, result);
    }

    /**
     * Attempts to read a file containing two lines of text.
     * 
     * @throws OCRException
     *             expected exception, since the ocrLineMethod should not accept images with more than a single line
     * @throws IOException
     *             unexpected
     */
    @Test(expected = OCRException.class)
    public void testOcrLineMvdRTwoLines() throws OCRException, IOException {
        TesseractOCREngine engine = new TesseractOCREngine();

        BufferedImage image = ImageIO.read(new File("src/test/resources/MvdR-twolines.png"));

        engine.ocrLine(image);
    }

    /**
     * Attempts to read a file containing no text.
     * 
     * @throws OCRException
     *             expected exception, since the ocrLineMethod should not accept images without text
     * @throws IOException
     *             unexpected
     */
    @Test(expected = OCRException.class)
    public void testOcrLineEmpty() throws OCRException, IOException {
        TesseractOCREngine engine = new TesseractOCREngine();

        BufferedImage image = ImageIO.read(new File("src/test/resources/empty.png"));

        engine.ocrLine(image);
    }

    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterAkuma() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/akuma.png", Umvc3Character.AKUMA);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterAmmy() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/amaterasu.png", Umvc3Character.AMATERASU);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterArthur() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/arthur.png", Umvc3Character.ARTHUR);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterViper() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/viper.png", Umvc3Character.C_VIPER);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterCap() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/cap.png", Umvc3Character.CAPTAIN_AMERICA);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterChun() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/chun.png", Umvc3Character.CHUN_LI);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterDante() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/dante.png", Umvc3Character.DANTE);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterDeadpool() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/deadpool.png", Umvc3Character.DEADPOOL);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterDormammu() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/dormammu.png", Umvc3Character.DORMAMMU);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterDoom() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/doom.png", Umvc3Character.DOCTOR_DOOM);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterStrange() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/strange.png", Umvc3Character.DOCTOR_STRANGE);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterFrank() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/frank.png", Umvc3Character.FRANK_WEST);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterFelicia() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/felicia.png", Umvc3Character.FELICIA);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterGhostRider() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/ghostrider.png", Umvc3Character.GHOST_RIDER);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterHaggar() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/haggar.png", Umvc3Character.HAGGAR);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterHawkeye() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/hawkeye.png", Umvc3Character.HAWKEYE);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterHulk() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/hulk.png", Umvc3Character.HULK);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterIronFist() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/ironfist.png", Umvc3Character.IRON_FIST);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterJill() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/jill.png", Umvc3Character.JILL);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterMagneto() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/magneto.png", Umvc3Character.MAGNETO);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterMorrigan() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/morrigan.png", Umvc3Character.MORRIGAN);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterNemesis() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/nemesis.png", Umvc3Character.NEMESIS);
    }

    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterNova() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/nova.png", Umvc3Character.NOVA);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterPhoenix() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/phoenix.png", Umvc3Character.PHOENIX);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterPhoenix2() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/phoenix2.png", Umvc3Character.PHOENIX);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterWright() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/wright.png", Umvc3Character.PHOENIX_WRIGHT);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterRyu() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/ryu.png", Umvc3Character.RYU);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterSent() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/sentinel.png", Umvc3Character.SENTINEL);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterSheHulk() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/shehulk.png", Umvc3Character.SHE_HULK);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterShuma() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/shuma-Gorath.png", Umvc3Character.SHUMA_GORATH);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterShumaInverted() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/shuma-Gorath-inverted.png", Umvc3Character.SHUMA_GORATH);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterSpencer() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/spencer.png", Umvc3Character.SPENCER);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterSpiderMan() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/spiderman.png", Umvc3Character.SPIDER_MAN);
    }
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterStorm() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/storm.png", Umvc3Character.STORM);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterStrider() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/strider.png", Umvc3Character.STRIDER_HIRYU);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterSuperSkrull() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/skrull.png", Umvc3Character.SUPER_SKRULL);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterTaskmaster() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/taskmaster.png", Umvc3Character.TASKMASTER);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterThor() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/thor.png", Umvc3Character.THOR);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterTrish() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/trish.png", Umvc3Character.TRISH);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterTron() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/tron.png", Umvc3Character.TRON);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterVergil() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/vergil.png", Umvc3Character.VERGIL);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterJoe() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/joe.png", Umvc3Character.VIEWTIFUL_JOE);
    }
    
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterWesker() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/wesker.png", Umvc3Character.WESKER);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterWolvie() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/wolverine.png", Umvc3Character.WOLVERINE);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterX23() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/x23.png", Umvc3Character.X_23);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterZero() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/zero.png", Umvc3Character.ZERO);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testOcrCharacterZeroInverted() throws OCRException, IOException {
        testOcrCharacter("src/test/resources/zero-inverted.png", Umvc3Character.ZERO);
    }
    
    /**
     * Attempts to read a file containing a character name.
     * 
     * @param filename
     *            file to be recognised
     * @param expectedCharacter
     *            expected character
     * @throws OCRException
     *             unexpected
     * @throws IOException
     *             unexpected
     */
    private void testOcrCharacter(String filename, Umvc3Character expectedCharacter) throws OCRException, IOException {
        TesseractOCREngine engine = new TesseractOCREngine();

        BufferedImage image = ImageIO.read(new File(filename));

        Umvc3Character character = engine.ocrCharacter(image);

        Assert.assertEquals(expectedCharacter, character);
    }
}
