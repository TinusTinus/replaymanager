package nl.tinus.umvc3replayanalyser.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Main class, used to start the application. Defines the JavaFX user interface.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3ReplayManager extends Application {
    /** URL of the program icon. */
    // TODO resize this icon, or replace it by a better one
    private static final String ICON = "ultimate-marvel-vs-capcom-3.jpg";
    /** Title of the application. */
    private static final String TITLE = "Ultimate Marvel vs Capcom 3 Replay Manager";

    /**
     * Main method, starts the application.
     * 
     * @param args
     *            command line parameters, which are compleyely ignored
     */
    public static void main(String[] args) {
        launch();
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        try {
            log.info("Starting application.");
            Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));

            log.info("Fxml loaded, performing additional initialisation.");
            stage.setTitle(TITLE);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(ICON));

            log.info("Showing UI.");
            stage.show();

            log.info("Application started.");
        } catch (Exception e) {
            log.error("Unable to start application.", e);
            handleExceptionOnStartup(stage, e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void stop() {
        log.info("Stopping application.");
    }

    /**
     * Handles an exception that caused program startup to fail, by showing an error message to the user.
     * 
     * @param stage
     *            stage
     * @param exception
     *            exception that caused startup to fail
     */
    private void handleExceptionOnStartup(Stage stage, Exception exception) {
        log.info("Showing error message dialog to indicate that startup failed.");

        // Create the error dialog programatically without relying on FXML, to minimize the chances of further failure.

        stage.setTitle(TITLE + " - failed to start up");

        // Error message text.
        Text text = new Text(TITLE + " was unable to start due to an unexpected problem.");

        // Text area containing the stack trace. Not visible by default.
        final TextArea stackTraceArea = new TextArea(ExceptionUtils.getStackTrace(exception));
        stackTraceArea.setEditable(false);
        stackTraceArea.setVisible(false);

        // Details button for displaying the stack trace.
        Button detailsButton = new Button();
        detailsButton.setText("Details");
        detailsButton.setOnAction(new EventHandler<ActionEvent>() {
            /** {@inheritDoc} */
            @Override
            public void handle(ActionEvent event) {
                log.info("User clicked Details.");
                stackTraceArea.setVisible(!stackTraceArea.isVisible());
            }
        });

        // OK button for closing the dialog.
        Button okButton = new Button();
        okButton.setText("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            /** {@inheritDoc} */
            @Override
            public void handle(ActionEvent event) {
                log.info("User clicked OK, exiting the program.");
                Platform.exit();
            }
        });

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

        // Use the standard program icon if possible.
        try {
            stage.getIcons().add(new Image(ICON));
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Failed to load icon for error dialog; proceeding with default JavaFX icon.", e);
        }

        stage.show();

        // Default size should also be the minimum size.
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());

        log.info("Error dialog displayed.");
    }
}
