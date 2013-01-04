package nl.tinus.umvc3replayanalyser.config;

/**
 * Dummy implementation of the Configuration interface.
 * 
 * @author Martijn van de Rijdt
 */
public class ConfigurationDummy implements Configuration {
    /** {@inheritDoc} */
    @Override
    public String getTesseractExecutablePath() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getDataDirectoryPath() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMoveVideoFilesToDataDirectory() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSavePreviewImageToDataDirectory() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
