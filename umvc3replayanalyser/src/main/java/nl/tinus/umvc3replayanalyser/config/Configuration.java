package nl.tinus.umvc3replayanalyser.config;

/**
 * Configuration of the application.
 * 
 * @author Martijn van de Rijdt
 */
public interface Configuration {
    /** 
     * Gets the path of the Tesseract executable.
     * 
     * @return path of the Tesseract executable
     */
    String getTesseractExecutablePath();
}
