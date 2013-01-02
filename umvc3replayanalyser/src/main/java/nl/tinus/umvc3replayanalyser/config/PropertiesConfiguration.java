package nl.tinus.umvc3replayanalyser.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Implementation of the configuration interface, using a properties file by the name of CONFIG_FILE_NAME.
 * 
 * @author Martijn van de Rijdt
 */
public class PropertiesConfiguration implements Configuration {
    /** Filename of the configuration file. */
    private static final String CONFIG_FILE_NAME = "/configuration.properties";

    /** Properties. */
    private final Properties properties;

    /** Constructor. */
    public PropertiesConfiguration() {
        super();
        
        InputStream stream = this.getClass().getResourceAsStream(CONFIG_FILE_NAME);
        if (stream == null) {
            throw new IllegalStateException(
                    String.format(
                            "No configuration file found by the name of %s. Please make sure such a file exists in the etc directory.",
                            CONFIG_FILE_NAME));
        }
        this.properties = new Properties();
        try {
            this.properties.load(stream);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalStateException(
                    String.format(
                            "Unable to read configuration from file: %s. Please check the contents and permissions of this file in the etc directory.",
                            CONFIG_FILE_NAME), e);
        }
    }

    /**
     * Retrieves a property from the properties; if the property value is null, this method throws an
     * IllegalStateException.
     * 
     * @param key
     *            a key, for which there is no meaningful default value
     * @return property value
     */
    private String getProperty(String key) {
        String result = properties.getProperty(key);
        if (result == null) {
            throw new IllegalStateException(
                    String.format(
                            "Missing property value: %s. Please make sure this property is defined in %s, located in the etc directory.",
                            key, CONFIG_FILE_NAME));
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String getTesseractExecutablePath() {
        return getProperty("tesseract-executable-path");
    }
}
