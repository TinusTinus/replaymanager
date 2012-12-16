package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Replay;

/**
 * Controller for the import replay view.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class ImportReplayPopupController {
    /** Task to be performed. */
    private final Task<List<Replay>> task;
    
    /** Progress bar. */
    @FXML
    private ProgressBar progressBar;
    /** Text area. */
    @FXML
    private TextArea textArea;
    
    /**
     * Constructor.
     * 
     * @param directory directory to load replays from
     * @param listeners listeners
     */
    public ImportReplayPopupController(File directory, List<ImportReplayListener> listeners) {
        super();
        this.task = new ImportReplayTask(directory, listeners);
    }
    
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
                
                // TODO close this window
            }
        };
        task.setOnSucceeded(eventHandler);
        task.setOnCancelled(eventHandler);
        task.setOnFailed(eventHandler);
        new Thread(task, "Replay Import Thread").start();
    }
}
