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
package nl.mvdr.umvc3replayanalyser.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.config.Configuration;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Replay;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 * Responsible for saving replay details.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class ReplaySaver {
    /** Name of the image format to be used when saving preview images. */
    private static final String IMAGE_FORMAT = "png";
    /** Extension for replay files. */
    private static final String REPLAY_EXTENSION = ".replay";
    /** Prefix in local file URLs. */
    private static final String FILE_URL_PREFIX = "file:///";

    /** Configuration. */
    private Configuration configuration;
    /** JSON object writer, used to save replays as files. */
    private final ObjectWriter writer;

    /**
     * Constructor.
     * 
     * @param configuration
     *            configuration of the application
     */
    ReplaySaver(Configuration configuration) {
        super();
        if (configuration == null) {
            throw new NullPointerException("configuration");
        }
        this.configuration = configuration;

        ObjectMapper mapper = new ObjectMapper();
        if (this.configuration.isPrettyPrintReplays()) {
            this.writer = mapper.writerWithDefaultPrettyPrinter();
        } else {
            this.writer = mapper.writer();
        }
    }

    /**
     * Constructs a replay for the given video file with the data from the given game, saves it to disk and returns it.
     * 
     * @param file
     *            original video file
     * @param game
     *            game
     * @param previewImage
     *            preview image
     * @param logger
     *            logger used to log informational messages that may be of interest to the user
     * @return the new replay
     * @throws IOException
     *             in case saving the replay fails
     */
    Replay saveReplay(File file, Game game, BufferedImage previewImage, MessageLogger logger) throws IOException {
        Date creationTime = new Date(file.lastModified());
        String baseFilename = game.getBaseFilename(creationTime);

        // Save the preview image.
        File previewImageFile;
        if (this.configuration.isSavePreviewImageToDataDirectory()) {
            // Create preview image file in the data directory.
            previewImageFile = new File(configuration.getDataDirectoryPath() + FileUtils.SEPARATOR + baseFilename + "."
                    + IMAGE_FORMAT);
            if (previewImageFile.exists()) {
                throw new IOException("Preview image file already exists: " + previewImageFile);
            }
        } else {
            // Save the preview image as a temporary file.
            previewImageFile = File.createTempFile("preview-" + baseFilename + "-", "." + IMAGE_FORMAT);
            previewImageFile.deleteOnExit();
        }
        try (ImageOutputStream stream = ImageIO.createImageOutputStream(previewImageFile)) {
            if (stream == null) {
                throw new IOException("Unable to save preview image. Image stream for path " + previewImageFile
                        + " could not be created.");
            }
            ImageIO.write(previewImage, IMAGE_FORMAT, stream);
            logger.log("Saved preview image: " + previewImageFile);
        }

        return saveReplay(file, game, FILE_URL_PREFIX + previewImageFile.getAbsolutePath(), logger);
    }

    /**
     * Constructs a replay for the given video file with the data from the given game, saves it to disk and returns it.
     * 
     * @param file
     *            original video file
     * @param game
     *            game
     * @param previewImageLocation
     *            location of the replay's preview image
     * @return the new replay
     * @throws IOException
     *             in case saving the replay fails
     */
    Replay saveReplay(File file, Game game, String previewImageLocation) throws IOException {
        MessageLogger logger = new MessageLogger() {
            /** {@inheritDoc} */
            @Override
            public void log(String message) {
                log.info(message);
            }
        };
        return saveReplay(file, game, previewImageLocation, logger);
    }

    /**
     * Constructs a replay for the given video file with the data from the given game, saves it to disk and returns it.
     * 
     * @param file
     *            original video file
     * @param game
     *            game
     * @param previewImageLocation
     *            location of the replay's preview image
     * @param logger
     *            logger used to log informational messages that may be of interest to the user
     * @return the new replay
     * @throws IOException
     *             in case saving the replay fails
     */
    Replay saveReplay(File file, Game game, String previewImageLocation, MessageLogger logger) throws IOException {
        Date creationTime = new Date(file.lastModified());
        String baseFilename = game.getBaseFilename(creationTime);

        File videoFile;
        if (this.configuration.isMoveVideoFilesToDataDirectory()) {
            // Move the replay to the data directory.
            String videoFileExtension;
            int index = file.getName().lastIndexOf('.');
            if (0 < index) {
                videoFileExtension = file.getName().substring(index).toLowerCase();
            } else {
                // No extension.
                videoFileExtension = "";
            }

            videoFile = new File(configuration.getDataDirectoryPath() + FileUtils.SEPARATOR + baseFilename
                    + videoFileExtension);

            // Note that move will fail with an IOException if videoFile aleady exists.
            Files.move(file.toPath(), videoFile.toPath());

            logger.log("Moved video file to: " + videoFile);
        } else {
            // Leave the video file where it is.
            videoFile = file;
        }

        String videoFileRelativePath = FileUtils.getRelativePath(videoFile.getAbsolutePath(), 
                configuration.getDataDirectory().getAbsolutePath());
        Replay replay = new Replay(creationTime, game, videoFileRelativePath, previewImageLocation);

        // Save replay to the data directory.
        File replayFile = new File(configuration.getDataDirectoryPath() + FileUtils.SEPARATOR + baseFilename
                + REPLAY_EXTENSION);
        if (replayFile.exists()) {
            throw new IOException("Replay already exists: " + replayFile);
        }
        writer.writeValue(replayFile, replay);
        logger.log("Saved replay file: " + replayFile);

        return replay;
    }

    /**
     * Edits a replay.
     * 
     * @param oldReplay
     *            replay to be edited
     * @param newGame
     *            game, containing the new replay details to be used
     * @return new replay
     * @throws IOException
     *             in case saving the new replay fails
     */
    Replay editReplay(Replay oldReplay, Game newGame) throws IOException {
        Replay result;
        if (oldReplay.getGame().equals(newGame)) {
            log.info("Replay remains unchanged.");
            result = oldReplay;
        } else {
            String oldBaseFilename = oldReplay.getGame().getBaseFilename(oldReplay.getCreationTime());

            String videoFilePath = FilenameUtils.normalize(this.configuration.getDataDirectory().getAbsolutePath()
                    + FileUtils.SEPARATOR + oldReplay.getVideoLocation());
            File videoFile = new File(videoFilePath);
            Date creationTime = new Date(videoFile.lastModified());
            String newBaseFilename = newGame.getBaseFilename(creationTime);

            String newPreviewImageFileLocation;
            if (this.configuration.isSavePreviewImageToDataDirectory()) {
                // Move preview image.
                String oldPreviewImageFileLocation = oldReplay.getPreviewImageLocation();
                if (oldPreviewImageFileLocation.startsWith(FILE_URL_PREFIX)) {
                    oldPreviewImageFileLocation = oldPreviewImageFileLocation.substring(FILE_URL_PREFIX.length());
                }
                File oldPreviewImageFile = new File(oldPreviewImageFileLocation);
                String previewImageExtension;
                int index = oldPreviewImageFileLocation.lastIndexOf('.');
                if (0 < index) {
                    previewImageExtension = oldPreviewImageFileLocation.substring(index).toLowerCase();
                } else {
                    // No extension.
                    previewImageExtension = "";
                }
                File previewImageFile = new File(configuration.getDataDirectoryPath() + FileUtils.SEPARATOR
                        + newBaseFilename + previewImageExtension);

                // Note that move will fail with an IOException if previewImageFile aleady exists, but that it will
                // succeed if the old and new paths are the same.
                Files.move(oldPreviewImageFile.toPath(), previewImageFile.toPath());
                newPreviewImageFileLocation = FILE_URL_PREFIX + previewImageFile.getAbsolutePath();
                log.info(String.format("Moved preview image from %s to %s.", oldPreviewImageFile, previewImageFile));
            } else {
                // Leave preview image wherever it is.
                newPreviewImageFileLocation = oldReplay.getPreviewImageLocation();
            }

            // Delete the old replay file.
            File oldReplayFile = new File(configuration.getDataDirectoryPath() + FileUtils.SEPARATOR + oldBaseFilename
                    + REPLAY_EXTENSION);
            Files.delete(oldReplayFile.toPath());
            log.info(String.format("Deleted old replay file: %s.", oldReplayFile));

            result = saveReplay(videoFile, newGame, newPreviewImageFileLocation);
        }
        return result;
    }

}
