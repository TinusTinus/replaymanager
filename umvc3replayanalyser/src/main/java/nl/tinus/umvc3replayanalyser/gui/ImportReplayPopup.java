package nl.tinus.umvc3replayanalyser.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class used to load and show the import replay view.
 * 
 * This is a utility class that cannot be instantiated; use the static method.
 * 
 * @author Martijn van de Rijdt
 */
// private constructor to prevent utility class instantiation
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ImportReplayPopup {
    /**
     * Shows the import replay popup in a new stage.
     * 
     * @param controller
     *            controller for this popup window, typically an ImportReplayPopupController
     */
    public static void show(Object controller) {
        show(new Stage(), controller);
    }
    
    /**
     * Shows the import replay popup.
     * 
     * @param stage
     *            stage in which to show the user interface
     * @param controller
     *            controller for this popup window, typically an ImportReplayPopupController
     */
    static void show(Stage stage, Object controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(ImportReplayPopup.class.getResource("/import-replay-popup.fxml"));
        fxmlLoader.setController(controller);
        try {
            Parent root = (Parent) fxmlLoader.load();

            log.info("Fxml loaded, performing additional initialisation.");
            stage.setTitle("Importing replays");
            stage.setScene(new Scene(root));

            log.info("Showing UI.");
            stage.show();

            // Default size should also be the minimum size.
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to parse FXML.", e);
        }
    }
}
