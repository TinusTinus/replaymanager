package nl.tinus.umvc3replayanalyser.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class, used to start the application. Defines the JavaFX user interface.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3ReplayManager extends Application {
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
            stage.setTitle("Ultimate Marvel vs Capcom 3 Replay Manager");
            stage.setScene(new Scene(root));
            // TODO resize this icon, maybe get the UMvC3 player icons?
            stage.getIcons().add(new Image("ultimate-marvel-vs-capcom-3.jpg"));

            log.info("Showing UI.");
            stage.show();

            log.info("Application started.");
        } catch (Exception e) {
            log.error("Unable to start application.", e);
            // TODO show a simple error message dialog to the user, which does not require fxml
        }
    }
}
