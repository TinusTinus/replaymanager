package nl.tinus.umvc3replayanalyser.controller;

import java.util.concurrent.ExecutionException;

import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the import replay view.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor
public class ImportReplayPopupController {
    /** Task to be performed. */
    private final Task<?> task;
    /** Boolean property which will be set to false once done. */
    private final BooleanProperty working;

    /** Progress bar. */
    @FXML
    private ProgressBar progressBar;
    /** Text area. */
    @FXML
    private TextArea textArea;

    /** Initialisation method. */
    @FXML
    private void initialize() {
        log.info("Performing controller initialisation.");
        
        progressBar.progressProperty().bind(task.progressProperty());
        textArea.textProperty().bind(task.messageProperty());
        EventHandler<WorkerStateEvent> eventHandler = new EventHandler<WorkerStateEvent>() {
            /** {@inheritDoc} */
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    task.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Unable to load replays.", e);
                }

                working.set(false);
                getApplicationWindow().hide();
            }
        };
        task.setOnSucceeded(eventHandler);
        task.setOnCancelled(eventHandler);
        task.setOnFailed(eventHandler);
        new Thread(task, "Replay Import Thread").start();
    }

    /**
     * Returns the popup window.
     * 
     * @return window
     */
    private Window getApplicationWindow() {
        return this.progressBar.getScene().getWindow();
    }
}
