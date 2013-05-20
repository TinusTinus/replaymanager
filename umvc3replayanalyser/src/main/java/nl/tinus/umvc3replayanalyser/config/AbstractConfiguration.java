package nl.tinus.umvc3replayanalyser.config;

import java.io.File;

/**
 * Abstract implementation of the Configuration interface.
 * 
 * @author Martijn van de Rijdt
 */
abstract class AbstractConfiguration implements Configuration {
    /** {@inheritDoc} */
    @Override
    public File getDataDirectory() {
        return new File(getDataDirectoryPath());
    }
}
