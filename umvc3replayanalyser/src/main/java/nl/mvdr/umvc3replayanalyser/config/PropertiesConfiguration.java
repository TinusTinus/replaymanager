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
package nl.mvdr.umvc3replayanalyser.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the configuration interface, using a properties file by the name of CONFIG_FILE_NAME.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class PropertiesConfiguration extends AbstractConfiguration implements Configuration {
    /** Filename of the configuration file. */
    private static final String CONFIG_FILE_NAME = "/configuration.properties";

    /** Properties. */
    private final Properties properties;

    /** Constructor. */
    public PropertiesConfiguration() {
        this(CONFIG_FILE_NAME);
    }

    /**
     * Constructor that allows to pass in the configuration file name. Default visibility since this is intended only to
     * be used in test cases. Otherwise just use the default constructor.
     * 
     * @param configFileName
     *            filename of the configuration file
     */
    PropertiesConfiguration(String configFileName) {
        super();

        log.info("Loading configuration from " + configFileName);
        InputStream stream = this.getClass().getResourceAsStream(configFileName);
        if (stream == null) {
            throw new IllegalStateException(
                    String.format(
                            "No configuration file found by the name of %s. Please make sure such a file exists in the etc directory.",
                            configFileName));
        }
        this.properties = new Properties();
        try {
            this.properties.load(stream);
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalStateException(
                    String.format(
                            "Unable to read configuration from file: %s. Please check the contents and permissions of this file in the etc directory.",
                            configFileName), e);
        }

        log.info("Properties loaded. Values in file (not necessarily in this order):");
        for (Entry<Object, Object> entry : this.properties.entrySet()) {
            log.info(String.format("  %s = %s", entry.getKey(), entry.getValue()));
        }

        // Fail fast: check that each of the configuration properties can be retrieved; log them as well.
        log.info("Tesseract executable path: " + getTesseractExecutablePath());
        log.info("Data directory path: " + getDataDirectoryPath());
        log.info("Move video files to data directory: " + isMoveVideoFilesToDataDirectory());
        log.info("Save preview images to data direcory: " + isSavePreviewImageToDataDirectory());
        log.info("Pretty print replays: " + isPrettyPrintReplays());
        // Add any other configuration properties here!
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
        return getProperty(key, null);
    }

    /**
     * Retrieves a property from the properties; if the property value is null, this method returns the given default,
     * or throws an IllegalStateException if there is no default.
     * 
     * @param key
     *            a key, for which there is no meaningful default value
     * @param defaultValue
     *            the default value to be returned if the propeties do not contain the given key; may be null if there
     *            is no sensible default
     * @return property value
     */
    private String getProperty(String key, String defaultValue) {
        String result = properties.getProperty(key);
        if (result == null && defaultValue != null) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Missing property value: %s. Using default: %s", key, defaultValue));
            }
            result = defaultValue;
        } else if (result == null) {
            throw new IllegalStateException(
                    String.format(
                            "Missing property value without default: %s. Please make sure this property is defined in %s, located in the etc directory.",
                            key, CONFIG_FILE_NAME));
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String getTesseractExecutablePath() {
        return getProperty("tesseract-executable-path");
    }

    /** {@inheritDoc} */
    @Override
    public String getDataDirectoryPath() {
        return getProperty("data-directory-path", "../data");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMoveVideoFilesToDataDirectory() {
        String string = getProperty("move-video-files", "true");
        return Boolean.valueOf(string).booleanValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isSavePreviewImageToDataDirectory() {
        String string = getProperty("save-preview-images", "true");
        return Boolean.valueOf(string).booleanValue();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isPrettyPrintReplays() {
        String string = getProperty("pretty-print-replays", "false");
        return Boolean.valueOf(string).booleanValue();
    }
}
