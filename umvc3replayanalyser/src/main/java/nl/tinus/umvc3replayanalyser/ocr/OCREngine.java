package nl.tinus.umvc3replayanalyser.ocr;

import java.awt.image.BufferedImage;

import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

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
