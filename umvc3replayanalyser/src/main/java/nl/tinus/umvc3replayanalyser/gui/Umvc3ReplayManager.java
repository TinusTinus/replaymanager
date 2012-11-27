package nl.tinus.umvc3replayanalyser.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
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
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ultimate Marvel vs Capcom 3 Replay Manager");

        // TODO actual ui instead of hello world :)
        Button button = new Button();
        button.setText("Say 'Hello World'");
        button.setOnAction(new EventHandler<ActionEvent>() {
            /** {@inheritDoc} */
            @Override
            public void handle(ActionEvent event) {
                log.info("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(button);
        
        primaryStage.setScene(new Scene(root, 300, 250));
        
        // TODO resize this icon, maybe get the UMvC3 player icons?
        primaryStage.getIcons().add(new Image("ultimate-marvel-vs-capcom-3.jpg"));
        
        primaryStage.show();
    }
}
