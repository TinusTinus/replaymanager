package nl.tinus.umvc3replayanalyser.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class, used to start the application. Defines the JavaFX user interface.
 * 
 * @author Martijn van de Rijdt
 */
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
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));
        stage.setTitle("Ultimate Marvel vs Capcom 3 Replay Manager");
        stage.setScene(new Scene(root));
        // TODO resize this icon, maybe get the UMvC3 player icons?
        stage.getIcons().add(new Image("ultimate-marvel-vs-capcom-3.jpg"));
        stage.show();
    }
}
