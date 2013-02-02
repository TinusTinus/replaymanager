package nl.tinus.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;

import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.ocr.OCRException;

/**
 * Analyses an image of an Ultimate Marvel vs Capcom 3 versus screen, using optical character recognition (OCR) to
 * determine player and character names.
 * 
 * @author Martijn van de Rijdt
 */
public interface VersusScreenAnalyser {

    /**
     * Analyses the given image and returns the Game represented by this image.
     * 
     * @param versusImage
     *            image to be analysed, should be a 1280 x 720 versus screen
     * @return game represented by this image; all fields are filled except winningSide and assists
     * @throws OCRException
     *             in case image analysis fails
     */
    // TODO drop the image size requirement by having all sizes be a percentage of the total image size.
    // Don't forget to fix Javadoc as well and reduce visibility of SCREEN_WIDTH and SCREEN_HEIGHT.
    public abstract Game analyse(BufferedImage versusImage) throws OCRException;
}