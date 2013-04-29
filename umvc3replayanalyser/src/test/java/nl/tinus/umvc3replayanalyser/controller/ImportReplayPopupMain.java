package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.config.Configuration;
import nl.tinus.umvc3replayanalyser.config.PropertiesConfiguration;
import nl.tinus.umvc3replayanalyser.gui.Popups;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyserImpl;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.ocr.OCREngine;
import nl.tinus.umvc3replayanalyser.ocr.TesseractOCREngine;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalyser;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalyserImpl;

/**
 * Main class which lets us test the import replay popup as a standalone application.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class ImportReplayPopupMain extends Application {

    /** Directory from which replays are loaded. */
    private static final String REPLAY_DIRECTORY = "src/test/resources";

    /**
     * Main method.
     * 
     * @param args
     *            command line parameters, which are passed on to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) throws IOException {
        log.info("Starting application.");

        final ArrayList<Replay> replays = new ArrayList<Replay>();
        
        SimpleBooleanProperty working = new SimpleBooleanProperty();
        working.addListener(new ChangeListener<Boolean>() {
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) {
                log.info("working changed from " + oldValue + " to " + newValue + "; " + replays.size() + " replays: "
                        + replays);
            }
        });

        Configuration configuration = new PropertiesConfiguration();
        OCREngine ocrEngine = new TesseractOCREngine(configuration);
        VersusScreenAnalyser versusScreenAnalyser = new VersusScreenAnalyserImpl(ocrEngine);
        ReplayAnalyser replayAnalyser = new ReplayAnalyserImpl(versusScreenAnalyser);
        ReplaySaver replaySaver = new ReplaySaver(configuration);
        ImportReplayTask task = new ImportReplayTask(new File(REPLAY_DIRECTORY), replays, configuration,
                replayAnalyser, replaySaver);
        ImportReplayPopupController controller = new ImportReplayPopupController(task, working, "Replay Import Thread");

        Popups.showImportReplaysPopup(stage, controller);

        log.info("Application started.");
    }
    
    /** {@inheritDoc} */
    @Override
    public void stop() {
        log.info("Stopping application.");
    }
}
