package nl.tinus.umvc3replayanalyser.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.controller.ImportReplayPopupController;
import nl.tinus.umvc3replayanalyser.model.Replay;

/**
 * Main class which lets us test the import replay popup as a standalone application.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class ImportReplayPopupMain extends Application {

    /** Directory from which replays are loaded. */
    // TODO use a directory in the workspace / repository
    private static final String REPLAY_DIRECTORY = "C:\\temp\\replays\\2replays";

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) throws IOException {
        log.info("Starting application.");

        final ArrayList<Replay> replays = new ArrayList<Replay>();
        
        SimpleBooleanProperty working = new SimpleBooleanProperty(true);
        working.addListener(new ChangeListener<Boolean>() {
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) {
                log.info("working changed from " + oldValue + " to " + newValue + "; replays: " + replays);
            }
        });

        ImportReplayPopupController controller = new ImportReplayPopupController(new File(REPLAY_DIRECTORY), replays,
                working);

        ImportReplayPopup.show(stage, controller);

        log.info("Application started.");
    }

    /**
     * Main class.
     * 
     * @param args
     *            command line parameters, which are passed on to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}
