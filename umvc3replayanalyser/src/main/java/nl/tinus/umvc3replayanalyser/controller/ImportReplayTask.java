package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.video.GameAndVersusScreen;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalyser;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalysisException;

/**
 * Task for importing replays.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class ImportReplayTask extends Task<List<Replay>> {
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
    /** Replay analyser. */
    private final ReplayAnalyser replayAnalyser;
    /** List of replays, to which the newly loaded replays will be added. */
    private final List<Replay> replays;
    /** Replay saver, used to actually save the replay file to disk. */
    private final ReplaySaver replaySaver;
    /** Message. */
    private String message;

    /**
     * Constructor.
     * 
     * @param directory
     *            directory
     * @param replays
     *            list of replays, to which the newly loaded replays will be added
     * @param replaySaver
     *            replay saver, used tp actually save the replay file to disk
     */
    ImportReplayTask(File directory, List<Replay> replays, ReplayAnalyser analyser, ReplaySaver replaySaver) {
        super();
        if (directory == null) {
            throw new NullPointerException("directory");
        }
        if (replays == null) {
            throw new NullPointerException("replays");
        }
        if (analyser == null) {
            throw new NullPointerException("analyser");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }
        if (replaySaver == null) {
            throw new NullPointerException("replaySaver");
        }

        this.directory = directory;
        this.replays = replays;
        this.replayAnalyser = analyser;
        this.replaySaver = replaySaver;

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

        MessageLogger logger = new MessageLogger() {
            /** {@inheritDoc} */
            @Override
            public void log(String message) {
                logMessage(message);                
            }
        };
        
        return this.replaySaver.saveReplay(file, gameAndVersusScreen.getGame(), gameAndVersusScreen.getVersusScreen(),
                logger);
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
