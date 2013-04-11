package nl.tinus.umvc3replayanalyser.gui;

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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Class used to load and show an error message popup.
 * 
 * Does not use FXML to minimise the chances of failure.
 * 
 * This is a utility class that cannot be instantiated; use the static methods.
 * 
 * @author Martijn van de Rijdt
 */
// private constructor to prevent utility class instantiation
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ErrorMessagePopup {
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
     */
    public static void show(String title, String errorMessage, final Stage stage, Exception exception) {
        log.info("Showing error message dialog to indicate that startup failed.");

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
                log.info("User clicked OK, closing the dialog.");
                stage.close();
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
     * Handles an exception that caused program startup to fail, by showing an error message to the user in a new stage.
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
}
