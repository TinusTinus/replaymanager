package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.config.Configuration;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Replay;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 * Responsible for saving replay details.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class ReplaySaver {
    /** Separator in file paths; "\" on Windows, "/" on Linux. */
    private static final String SEPARATOR = System.getProperty("file.separator");
    /** Extension for replay files. */
    private static final String REPLAY_EXTENSION = ".replay";
    
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

            videoFile = new File(configuration.getDataDirectoryPath() + SEPARATOR + baseFilename + videoFileExtension);

            // Note that move will fail with an IOException if videoFile aleady exists.
            Files.move(file.toPath(), videoFile.toPath());

            logger.log("Moved video file to: " + videoFile);
        } else {
            // Leave the video file where it is.
            videoFile = file;
        }

        Replay replay = new Replay(creationTime, game, videoFile.getAbsolutePath(), previewImageLocation);

        // Save replay to the data directory.
        File replayFile = new File(configuration.getDataDirectoryPath() + SEPARATOR + baseFilename + REPLAY_EXTENSION);
        if (replayFile.exists()) {
            throw new IOException("Replay already exists: " + replayFile);
        }
        writer.writeValue(replayFile, replay);
        logger.log("Saved replay file: " + replayFile);

        return replay;
    }

}
