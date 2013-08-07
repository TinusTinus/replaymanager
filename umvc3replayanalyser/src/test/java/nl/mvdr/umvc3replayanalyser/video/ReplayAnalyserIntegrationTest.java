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
package nl.mvdr.umvc3replayanalyser.video;

import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.config.Configuration;
import nl.mvdr.umvc3replayanalyser.config.PropertiesConfiguration;
import nl.mvdr.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.mvdr.umvc3replayanalyser.image.VersusScreenAnalyserImpl;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;
import nl.mvdr.umvc3replayanalyser.ocr.TesseractOCREngine;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ReplayAnalyser}.
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
        Configuration configuration = new PropertiesConfiguration();
        TesseractOCREngine ocrEngine = new TesseractOCREngine(configuration);
        this.versusScreenAnalyser = new VersusScreenAnalyserImpl(ocrEngine);
    }

    /**
     * Analyses a replay.
     * 
     * @throws ReplayAnalysisException
     *             unexpected
     */
    @Test
    public void test() throws ReplayAnalysisException {
        ReplayAnalyserImpl analyser = new ReplayAnalyserImpl(this.versusScreenAnalyser);

        GameAndVersusScreen gameAndVersusScreen = analyser.analyse("src/test/resources/badhyper-vs-MvdR.mp4");
        Game game = gameAndVersusScreen.getGame();
        
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
     * Attempts to analyse a file which can be decoded just fine, but does not contain a versus screen. This is expected
     * to fail with a nice ReplayAnalysisException.
     * 
     * @throws ReplayAnalysisException
     *             expected exception
     */
    @Test(expected = ReplayAnalysisException.class)
    public void testEOF() throws ReplayAnalysisException {
        ReplayAnalyserImpl analyser = new ReplayAnalyserImpl(this.versusScreenAnalyser);
        // Xuggle can handle png files, and interprets them as a single frame video.
        analyser.analyse("src/test/resources/vswithoutnames.png");
    }
    
    /**
     * Attempts to analyse a file that is not a video file. This is expected to fail with a nice ReplayAnalysisException.
     * 
     * @throws ReplayAnalysisException
     *             expected exception
     */
    @Test(expected = ReplayAnalysisException.class)
    public void testTextFile() throws ReplayAnalysisException {
        ReplayAnalyserImpl analyser = new ReplayAnalyserImpl(this.versusScreenAnalyser);
        analyser.analyse("src/test/resources/test.txt");
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
