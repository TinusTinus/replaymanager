package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Window;
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
    private final ImportReplayTask task;

    /** Progress bar. */
    @FXML
    private ProgressBar progressBar;
    /** Text area. */
    @FXML
    private TextArea textArea;

    /**
     * Constructor.
     * 
     * @param directory
     *            directory to load replays from
     * @param replays
     *            list of replays, to which newly loaded replays will be added
     */
    public ImportReplayPopupController(File directory, List<Replay> replays) {
        super();
        this.task = new ImportReplayTask(directory, replays);
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
