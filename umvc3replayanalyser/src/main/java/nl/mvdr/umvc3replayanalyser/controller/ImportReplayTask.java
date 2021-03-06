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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.model.Replay;
import nl.mvdr.umvc3replayanalyser.video.GameAndVersusScreen;
import nl.mvdr.umvc3replayanalyser.video.ReplayAnalyser;
import nl.mvdr.umvc3replayanalyser.video.ReplayAnalysisException;

/**
 * Task for importing replays.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class ImportReplayTask extends Task<List<Replay>> {
    /**
     * Thread-local variable holding the time format for log messages. This variable is stored as a thread-local instead
     * of just a single constant, because {@link SimpleDateFormat} is not threadsafe.
     */
    private static final ThreadLocal<DateFormat> LOG_MESSAGE_TIME_FORMAT = new ThreadLocal<DateFormat>() {
        /** {@inheritDoc} */
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss,SSS");
        }
    };

    /** The directory from which to import replays. */
    @NonNull
    private final File directory;
    /** Replay analyser. */
    @NonNull
    private final ReplayAnalyser replayAnalyser;
    /** List of replays, to which the newly loaded replays will be added. */
    @NonNull
    private final List<Replay> replays;
    /** Replay saver, used to actually save the replay file to disk. */
    @NonNull
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
    ImportReplayTask(@NonNull File directory, @NonNull List<Replay> replays, @NonNull ReplayAnalyser analyser,
            @NonNull ReplaySaver replaySaver) {
        super();

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
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
            public void log(String logMessage) {
                logMessage(logMessage);
            }
        };

        return this.replaySaver.saveReplay(file, gameAndVersusScreen.getGame(), gameAndVersusScreen.getVersusScreen(),
                logger);
    }

    /**
     * Logs the given message to the logger at log level INFO and appends it to the message property.
     * 
     * @param logMessage
     *            message text
     */
    private void logMessage(String logMessage) {
        log.info(message);
        this.message = this.message + "\n" + LOG_MESSAGE_TIME_FORMAT.get().format(new Date()) + " - " + logMessage;
        updateMessage(this.message);
    }
}
