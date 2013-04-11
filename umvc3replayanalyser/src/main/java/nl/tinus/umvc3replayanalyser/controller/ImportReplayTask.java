package nl.tinus.umvc3replayanalyser.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.config.Configuration;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.video.GameAndVersusScreen;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalyser;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalysisException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 * Task for importing replays.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class ImportReplayTask extends Task<List<Replay>> {
    /** Name of the image format to be used when saving preview images. */
    private static final String IMAGE_FORMAT = "png";
    /** Separator in file paths; "\" on Windows, "/" on Linux. */
    private static final String SEPARATOR = System.getProperty("file.separator");
    /**
     * Thread-local variable holding the time format for log messages. This variable is stored as a thread-local instead
     * of just a single constant, because SimpleDateFormat is not threadsafe.
     */
    private static final ThreadLocal<DateFormat> LOG_MESSAGE_TIME_FORMAT = new ThreadLocal<DateFormat>() {
        /** {@inheritDoc} */
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss,SSS");
        }
    };

    /** The directory from which to import replays. */
    private final File directory;
    /** Configuration of the application. */
    private final Configuration configuration;
    /** Replay analyser. */
    private final ReplayAnalyser replayAnalyser;
    /** List of replays, to which the newly loaded replays will be added. */
    private final List<Replay> replays;
    /** JSON object writer, used to save replays as files. */
    private final ObjectWriter writer;
    /** Message. */
    private String message;

    /**
     * Constructor.
     * 
     * @param directory
     *            directory
     * @param replays
     *            list of replays, to which the newly loaded replays will be added
     * @param configuration
     *            configuration of the application
     * @param analyser
     *            replay nalyser
     */
    ImportReplayTask(File directory, List<Replay> replays, Configuration configuration, ReplayAnalyser analyser) {
        super();
        if (directory == null) {
            throw new NullPointerException("directory");
        }
        if (replays == null) {
            throw new NullPointerException("replays");
        }
        if (configuration == null) {
            throw new NullPointerException("configuration");
        }
        if (analyser == null) {
            throw new NullPointerException("analyser");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        this.directory = directory;
        this.replays = replays;
        this.replayAnalyser = analyser;
        this.configuration = configuration;
        
        ObjectMapper mapper = new ObjectMapper();
        if (this.configuration.isPrettyPrintReplays()) {
            this.writer = mapper.writerWithDefaultPrettyPrinter();
        } else {
            this.writer = mapper.writer();
        }

        this.message = "";
    }

    /** {@inheritDoc} */
    @Override
    protected List<Replay> call() {
        logMessage("Importing replays from " + directory);
        List<File> files = createFileList(directory);
        logMessage(files.size() + " file(s) found.");
        int workDone = 0;
        updateProgress(workDone, files.size());
        for (File file : files) {
            logMessage("Trying to import: " + file);
            try {
                final Replay replay = importReplay(file);
                logMessage("Imported replay: " + replay.getGame());

                Platform.runLater(new Runnable() {
                    /** {@inheritDoc} */
                    @Override
                    public void run() {
                        replays.add(replay);
                    }
                });
            } catch (ReplayAnalysisException | IOException e) {
                logMessage(String.format("Unable to import file: %s. %s", file, e.getMessage()));
                log.info("Exception: ", e);
            }
            workDone++;
            updateProgress(workDone, files.size());
        }
        logMessage("Done.");
        return replays;
    }

    /**
     * Gives a list of all files in the given directory. Directories are not returned but are searched recursively.
     * 
     * @param directory
     *            directory
     * @return list of files
     */
    private List<File> createFileList(File sourceDirectory) {
        List<File> result = new ArrayList<>();
        for (File file : sourceDirectory.listFiles()) {
            if (file.isDirectory()) {
                result.addAll(createFileList(file));
            } else {
                result.add(file);
            }
        }
        return result;
    }

    /**
     * Imports the given file as a replay.
     * 
     * @param file
     *            file to be imported
     * @return replay
     * @throws ReplayAnalysisException
     *             in case the replay cannot be analysed
     * @throws IOException
     *             if unable to save the preview image, video file or replay file
     */
    private Replay importReplay(File file) throws ReplayAnalysisException, IOException {
        GameAndVersusScreen gameAndVersusScreen = this.replayAnalyser.analyse(file.getAbsolutePath());

        Date creationTime = new Date(file.lastModified());
        Game game = gameAndVersusScreen.getGame();
        BufferedImage versusScreen = gameAndVersusScreen.getVersusScreen();
        
        String baseFilename = game.getBaseFilename(creationTime);

        // Save the preview image.
        File previewImageFile;
        if (this.configuration.isSavePreviewImageToDataDirectory()) {
            // Create preview image file in the data directory.
            previewImageFile = new File(configuration.getDataDirectoryPath() + SEPARATOR + baseFilename + "."
                    + IMAGE_FORMAT);
            if (previewImageFile.exists()) {
                throw new IOException("Preview image file already exists: " + previewImageFile);
            }
        } else {
            // Save the preview image as a temporary file.
            previewImageFile = File.createTempFile("previewimage", "." + IMAGE_FORMAT);
            previewImageFile.deleteOnExit();
        }
        try (ImageOutputStream stream = ImageIO.createImageOutputStream(previewImageFile)) {
            if (stream == null) {
                throw new IOException("Unable to save preview image. Image stream for path " + previewImageFile
                        + " could not be created.");
            }
            ImageIO.write(versusScreen, IMAGE_FORMAT, stream);
            logMessage("Saved preview image: " + previewImageFile);
        }

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

            logMessage("Moved video file to: " + videoFile);
        } else {
            // Leave the video file where it is.
            videoFile = file;
        }

        Replay replay = new Replay(creationTime, game, videoFile.getAbsolutePath(), "file:///"
                + previewImageFile.getAbsolutePath());

        // Save replay to the data directory.
        File replayFile = new File(configuration.getDataDirectoryPath() + SEPARATOR + baseFilename + ".replay");
        if (replayFile.exists()) {
            throw new IOException("Replay already exists: " + replayFile);
        } else {
            this.writer.writeValue(replayFile, replay);
            logMessage("Saved replay file: " + replayFile);
        }

        return replay;
    }

    /**
     * Logs the given message to the logger at log level INFO and appends it to the message property.
     * 
     * @param message
     *            message
     */
    private void logMessage(String message) {
        log.info(message);
        this.message = this.message + "\n" + LOG_MESSAGE_TIME_FORMAT.get().format(new Date()) + " - " + message;
        updateMessage(this.message);
    }
}
