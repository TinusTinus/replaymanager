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
        
        final Task<List<Replay>> task = new Task<List<Replay>>() {
            /** {@inheritDoc} */
            @Override
            protected List<Replay> call() throws InterruptedException {
                log.info("Importing replays from " + directory);
                // TODO actually import replays
                String message = "";
                
                int numTurns = 100;
                for (int i = 0; i != numTurns; i++) {
                    updateProgress(i, numTurns);
                    message = message + i + "\n";
                    updateMessage(message);
                    Thread.sleep(5000 / numTurns);
                }
                updateProgress(numTurns, numTurns);
                message = message + "Done!";
                updateMessage(message);
                
                return null;
            }
        };
        
        progressBar.progressProperty().bind(task.progressProperty());
        textArea.textProperty().bind(task.messageProperty());
        EventHandler<WorkerStateEvent> eventHandler = new EventHandler<WorkerStateEvent>() {
            /** {@inheritDoc} */
            @Override
            public void handle(WorkerStateEvent event) {
                try {
                    List<Replay> replays = task.get();
                    log.info("Loaded replays: " + replays);
                    // TODO pass these on to the Umvc3ReplayManagerController
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Unable to load replays.", e);
                }
            }
        };
        task.setOnSucceeded(eventHandler);
        task.setOnCancelled(eventHandler);
        task.setOnFailed(eventHandler);
        new Thread(task, "Replay Import Thread").start();
    }
}
