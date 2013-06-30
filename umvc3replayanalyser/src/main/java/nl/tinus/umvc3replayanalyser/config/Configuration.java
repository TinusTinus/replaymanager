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
package nl.tinus.umvc3replayanalyser.config;

import java.io.File;

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
     * Gets the data directory, containing replay info.
     * 
     * @return data directory
     */
    File getDataDirectory();

    /**
     * Indicates whether, when importing new replays, the replay file should be moved to the data directory.
     * 
     * @return whether video files should be moved
     */
    boolean isMoveVideoFilesToDataDirectory();

    /**
     * Indicates whether, when importing new replays, the preview image should be saved to the data directory. If not,
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
