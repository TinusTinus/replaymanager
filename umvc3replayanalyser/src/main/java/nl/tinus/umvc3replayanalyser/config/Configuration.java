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

    /**
     * Gets the path of the data directory, containing replay info.
     * 
     * @return path of the data directory
     */
    String getDataDirectoryPath();

    /**
     * Indicates whether, when importing new replays, the replay file should be moved to the data directory.
     * 
     * @return whether video files should be moved
     */
    boolean isMoveVideoFilesToDataDirectory();
}
