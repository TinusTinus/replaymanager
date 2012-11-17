package nl.tinus.umvc3replayanalyser.video;

import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.ocr.OCREngine;
import nl.tinus.umvc3replayanalyser.ocr.TesseractOCREngine;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests ReplayAnalyser.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class ReplayAnalyserIntegrationTest {
    /** Versus screen analyser to be used. */
    private VersusScreenAnalyser versusScreenAnalyser;

    /** Setup method. */
    @Before
    public void setUp() {
        OCREngine ocrEngine = new TesseractOCREngine();
        this.versusScreenAnalyser = new VersusScreenAnalyser(ocrEngine);
    }

    /**
     * Analyses a replay.
     * 
     * @throws ReplayAnalysisException
     *             unexpected
     */
    @Test
    public void test() throws ReplayAnalysisException {
        ReplayAnalyser analyser = new ReplayAnalyser(this.versusScreenAnalyser);

        Game game = analyser.analyse("src/test/resources/badhyper-vs-MvdR.mp4");

        Assert.assertEquals(Umvc3Character.FELICIA, game.getTeamOne().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.THOR, game.getTeamOne().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.WESKER, game.getTeamOne().getThirdCharacter());

        Assert.assertEquals(Umvc3Character.WOLVERINE, game.getTeamTwo().getFirstCharacter());
        Assert.assertEquals(Umvc3Character.ZERO, game.getTeamTwo().getSecondCharacter());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, game.getTeamTwo().getThirdCharacter());

        assertPlayerName("badhyper", game.getPlayerOne().getGamertag());
        assertPlayerName("MvdR", game.getPlayerTwo().getGamertag());
    }
    
    /**
     * Checks the given player name.
     * 
     * @param expected
     *            expected name
     * @param actual
     *            actual name
     */
    private void assertPlayerName(String expected, String actual) {
        // OCR'ing player names is hard, so we're going to give Tesseract a break and allow for some mismatched
        // characters.
        int distance = StringUtils.getLevenshteinDistance(expected, actual);
        String message = String.format("Expected \"%s\", got \"%s\", Levenshtein distance: %s", expected, actual, ""
                + distance);
        log.info(message);
        Assert.assertTrue(message, distance < 5);
    }
}
