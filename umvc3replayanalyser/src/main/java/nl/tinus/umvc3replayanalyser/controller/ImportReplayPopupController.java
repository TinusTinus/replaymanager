/*
 * Copyright 2012, 2013 Martijn van de Rijdt 
 * 
 * This file is part of the Ultimate Marvel vs Capcom 3 Replay Manager.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with the Ultimate Marvel vs Capcom 3
 * Replay Manager. If not, see <http://www.gnu.org/licenses/>.
 */
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
import lombok.AccessLevel;
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
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ImportReplayPopupController {
    /** Task to be performed. */
    @NonNull
    private final Task<?> task;
    /** Boolean property which will be set to true upon initialisation, and to false once the task is done. */
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
        working.set(true);
        new Thread(task, threadName).start();
        log.info("Initialisation complete.");
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
