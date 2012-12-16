package nl.tinus.umvc3replayanalyser.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/import-replay-popup.fxml"));
        fxmlLoader.setController(new ImportReplayPopupController(new File(REPLAY_DIRECTORY), new ArrayList<Replay>(), new SimpleBooleanProperty()));
        Parent root = (Parent) fxmlLoader.load();

        log.info("Fxml loaded, performing additional initialisation.");
        stage.setTitle("Replay import popup tester");
        stage.setScene(new Scene(root));

        log.info("Showing UI.");
        stage.show();

        // Default size should also be the minimum size.
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());

        log.info("Application started.");
    }

    /**
     * Main class.
     * 
     * @param args command line parameters, which are passed on to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}
