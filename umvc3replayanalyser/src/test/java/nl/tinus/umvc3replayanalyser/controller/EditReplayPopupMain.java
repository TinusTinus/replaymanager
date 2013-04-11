package nl.tinus.umvc3replayanalyser.controller;

import java.io.IOException;

import nl.tinus.umvc3replayanalyser.gui.Popups;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class which lets us test the edit replay popup as a standalone application.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class EditReplayPopupMain extends Application {
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

        Popups.showEditReplayPopup(new EditReplayController(null));

        log.info("Application started.");
    }
    
    /** {@inheritDoc} */
    @Override
    public void stop() {
        log.info("Stopping application.");
    }
}
