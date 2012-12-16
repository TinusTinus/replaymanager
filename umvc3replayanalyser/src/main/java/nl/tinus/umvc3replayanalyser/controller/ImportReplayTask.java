package nl.tinus.umvc3replayanalyser.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.concurrent.Task;

import javax.imageio.ImageIO;

import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.ocr.TesseractOCREngine;
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
    /** The directory from which to import replays. */
    private final File directory;
    /** Replay analyser. */
    private final ReplayAnalyser replayAnalyser;
    /** Message. */
    private String message;
    
    ImportReplayTask(File directory) {
        super();
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }
        this.directory = directory;
        // TODO inject the replay analyser?
        this.replayAnalyser = new ReplayAnalyser(new VersusScreenAnalyser(new TesseractOCREngine()));
        this.message = "";
    }
    
    /** {@inheritDoc} */
    @Override
    protected List<Replay> call() {
        logMessage("Importing replays from " + directory);
        List<File> files = createFileList(directory);
        List<Replay> result = new ArrayList<Replay>();
        int workDone = 0;
        for (File file: files) {
            logMessage("Trying to import: " + file);
            try {
                Replay replay = importReplay(file);
                logMessage("Imported replay: " + replay.getGame());
                result.add(replay);
            } catch (ReplayAnalysisException | IOException e) {
                logMessage(String.format("Unable to import file: %s. ", file, e.getMessage()));
                log.info("Exception: ", e);
            }
            workDone++;
            updateProgress(workDone, files.size());
        }
        logMessage("Done.");
        return result;
    }
    
    /**
     * Gives a list of all files in the given directory. Directories are not returned but are searched recursively.
     * 
     * @param directory directory
     * @return list of files
     */
    private List<File> createFileList(File sourceDirectory) {
        List<File> result = new ArrayList<>();
        for (File file: sourceDirectory.listFiles()) {
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
     *             if unable to save the preview image or replay video
     */
    private Replay importReplay(File file) throws ReplayAnalysisException, IOException {
        GameAndVersusScreen gameAndVersusScreen = this.replayAnalyser.analyse(file.getAbsolutePath());

        Game game = gameAndVersusScreen.getGame();
        BufferedImage versusScreen = gameAndVersusScreen.getVersusScreen();

        // TODO Save the preview image and move the replay to the data directory.
        // For now, save the preview image in a temp directory and leave the replay where it is.
        File previewImageFile = File.createTempFile("previewimage", ".png");
        previewImageFile.deleteOnExit();
        ImageIO.write(versusScreen, "png", previewImageFile);

        Date creationTime = new Date(file.lastModified());

        return new Replay(creationTime, game, "file:///" + file.getAbsolutePath(), "file:///"
                + previewImageFile.getAbsolutePath());
    }
    
    /**
     * Logs the given message to the logger at log level INFO and appends it to the message property.
     * 
     * @param message
     *            message
     */
    private void logMessage(String message) {
        log.info(message);
        this.message = this.message + "\n" + message; 
        updateMessage(this.message);
    }
}
