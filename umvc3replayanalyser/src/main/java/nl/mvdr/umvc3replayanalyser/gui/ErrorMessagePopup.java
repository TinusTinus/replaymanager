// Generated by delombok at Sat Feb 08 23:15:03 CET 2014
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
package nl.mvdr.umvc3replayanalyser.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.apache.commons.lang3.exception.ExceptionUtils;

// private constructor to prevent utility class instantiation

/**
 * Class used to load and show an error message popup.
 *
 * Does not use FXML to minimise the chances of failure.
 *
 * This is a utility class that cannot be instantiated; use the static methods.
 *
 * @author Martijn van de Rijdt
 */
public class ErrorMessagePopup {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ErrorMessagePopup.class);

    /**
     * Handles an exception that caused program startup to fail, by showing an error message to the user.
     *
     * @param title
     *            title for the dialog
     * @param errorMessage
     *            error message
     * @param stage
     *            stage in which to show the error message
     * @param exception
     *            exception that caused the error
     * @param okHandler
     *            handler for when the user presses the OK button
     */
    public static void show(String title, String errorMessage, final Stage stage, Throwable exception, EventHandler<ActionEvent> okHandler) {
        log.info("Showing error message dialog.");
        // Create the error dialog programatically without relying on FXML, to minimize the chances of further failure.
        stage.setTitle(title);
        // Error message text.
        Text text = new Text(errorMessage);
        // Text area containing the stack trace. Not visible by default.
        String stackTrace;
        if (exception != null) {
            stackTrace = ExceptionUtils.getStackTrace(exception);
        } else {
            stackTrace = "No more details available.";
        }
        final TextArea stackTraceArea = new TextArea(stackTrace);
        stackTraceArea.setEditable(false);
        stackTraceArea.setVisible(false);
        // Details button for displaying the stack trace.
        Button detailsButton = new Button();
        detailsButton.setText("Details");
        detailsButton.setOnAction(event -> {
            log.info("User clicked Details.");
            stackTraceArea.setVisible(!stackTraceArea.isVisible());
        });
        // OK button for closing the dialog.
        /** {@inheritDoc} */
        Button okButton = new Button();
        okButton.setText("OK");
        okButton.setOnAction(okHandler);
        // Horizontal box containing the buttons, to make sure they are always centered.
        HBox buttonsBox = new HBox(5);
        buttonsBox.getChildren().add(detailsButton);
        buttonsBox.getChildren().add(okButton);
        buttonsBox.setAlignment(Pos.CENTER);
        // Layout constraints.
        AnchorPane.setTopAnchor(text, Double.valueOf(5));
        AnchorPane.setLeftAnchor(text, Double.valueOf(5));
        AnchorPane.setRightAnchor(text, Double.valueOf(5));
        AnchorPane.setTopAnchor(stackTraceArea, Double.valueOf(31));
        AnchorPane.setLeftAnchor(stackTraceArea, Double.valueOf(5));
        AnchorPane.setRightAnchor(stackTraceArea, Double.valueOf(5));
        AnchorPane.setBottomAnchor(stackTraceArea, Double.valueOf(36));
        AnchorPane.setLeftAnchor(buttonsBox, Double.valueOf(5));
        AnchorPane.setRightAnchor(buttonsBox, Double.valueOf(5));
        AnchorPane.setBottomAnchor(buttonsBox, Double.valueOf(5));
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(text, stackTraceArea, buttonsBox);
        stage.setScene(new Scene(root));
        // Use a standard program icon if possible.
        try {
            stage.getIcons().add(Icons.get().getRandomPortrait());
        } catch (IllegalStateException e) {
            log.warn("Failed to load icon for error dialog; proceeding with default JavaFX icon.", e);
        }
        stage.show();
        // Default size should also be the minimum size.
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        log.info("Error dialog displayed.");
    }
    
    /**
     * Handles an exception that caused program startup to fail, by showing an error message to the user. The OK button
     * closes the stage.
     *
     * @param title
     *            title for the dialog
     * @param errorMessage
     *            error message
     * @param stage
     *            stage in which to show the error message
     * @param exception
     *            exception that caused the error
     */
    public static void show(String title, String errorMessage, final Stage stage, Exception exception) {
        EventHandler<ActionEvent> okHandler = event -> {
            log.info("User clicked OK, closing the dialog.");
            stage.close();
        };
        show(title, errorMessage, stage, exception, okHandler);
    }

    /**
     * Handles an exception that caused program startup to fail, by showing an error message to the user in a new stage.
     * The OK button closes the stage.
     *
     * @param title
     *            title for the dialog
     * @param errorMessage
     *            error message
     * @param exception
     *            exception that caused the error
     */
    public static void show(String title, String errorMessage, Exception exception) {
        show(title, errorMessage, new Stage(), exception);
    }
    
    /**
     * Handles an exception that caused program startup to fail, by showing an error message to the user in a new stage.
     *
     * @param title
     *            title for the dialog
     * @param errorMessage
     *            error message
     * @param exception
     *            exception that caused the error
     * @param okHandler
     *            handler for when the user presses the OK button
     */
    public static void show(String title, String errorMessage, Throwable exception, EventHandler<ActionEvent> okHandler) {
        show(title, errorMessage, new Stage(), exception, okHandler);
    }

    @java.lang.SuppressWarnings("all")
    private ErrorMessagePopup() {
    }
}