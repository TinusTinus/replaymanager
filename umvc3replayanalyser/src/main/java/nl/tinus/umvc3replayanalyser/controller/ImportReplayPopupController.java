package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Replay;

/**
 * Controller for the import replay view.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor
public class ImportReplayPopupController {
    /** Directory to load replays from. */
    private final File directory;
    /** Listeners. */
    private final List<ImportReplayListener> listeners;
    
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
        
        final Task<List<Replay>> task = new ImportReplayTask(directory);
        
        progressBar.progressProperty().bind(task.progressProperty());
        textArea.textProperty().bind(task.messageProperty());
        EventHandler<WorkerStateEvent> eventHandler = new EventHandler<WorkerStateEvent>() {
            /** {@inheritDoc} */
            @Override
            public void handle(WorkerStateEvent event) {
                List<Replay> replays;
                try {
                    replays = task.get();
                    log.info("Loaded replays: " + replays);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Unable to load replays.", e);
                    replays = Collections.emptyList();
                }
                
                for (ImportReplayListener listener: listeners) {
                    listener.replaysImported(replays);
                }
            }
        };
        task.setOnSucceeded(eventHandler);
        task.setOnCancelled(eventHandler);
        task.setOnFailed(eventHandler);
        new Thread(task, "Replay Import Thread").start();
    }
}
