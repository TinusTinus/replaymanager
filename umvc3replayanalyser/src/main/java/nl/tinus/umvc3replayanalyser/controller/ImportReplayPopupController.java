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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the import replay view.
 * 
 * @author Martijn van de Rijdt
 */
// Note: both this controller and the corresponding FXML form have no knowledge of what type of task is being performed.
// They would be very easy to convert into a general "perform task popup" and its controller.
@Slf4j
@RequiredArgsConstructor
public class ImportReplayPopupController {
    /** Task to be performed. */
    @NonNull
    private final Task<?> task;
    /** Boolean property which will be set to false once done. */
    @NonNull
    private final BooleanProperty working;
    /** Name for the newly constructed thread that will perform the task. */
    private final String threadName;

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
                    log.error("Unable to perform task succesfully.", e);
                }
                working.set(false);
                getApplicationWindow().hide();
            }
        };
        task.setOnSucceeded(eventHandler);
        task.setOnCancelled(eventHandler);
        task.setOnFailed(eventHandler);
        new Thread(task, threadName).start();
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
