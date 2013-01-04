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

    /**
     * Indicates whether, when importing new replays, the preview image should be saved to the data direectory. If not,
     * it is only saved as a temporary file. This should normally always be set to true, but it may be useful to set it
     * to false in development environments.
     * 
     * @return whether preview images should be saved to the data directory
     */
    boolean isSavePreviewImageToDataDirectory();

    /**
     * Indicates whether the contents of .replay files should be pretty printed. Pretty printing makes these files more
     * readily human-readable, but increases the file size significantly.
     * 
     * @return whether pretty printing should be used
     */
    boolean isPrettyPrintReplays();
}
