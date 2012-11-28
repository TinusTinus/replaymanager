package nl.tinus.umvc3replayanalyser.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

/**
 * Main controller class for this JavaFX application. Referenced from the corresponding fxml.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3ReplayManagerController {
    /** Preview image view. */
    @FXML
    private ImageView previewImageView;
    /** Anchor pane containing the preview image view. */
    @FXML
    private AnchorPane previewImageAnchorPane;
    /** The default preview image. Whenever no replay is selected, this one should be displayed. */
    @FXML
    private Image defaultPreviewImage;
    /** The main table view. */
    @FXML
    private TableView<Game> replayTableView;

    /** Initialisation method. */
    @FXML
    public void initialize() {
        log.info("Performing controller initialisation.");

        // Bind the image size to the size of its parent.
        previewImageView.fitWidthProperty().bind(previewImageAnchorPane.widthProperty());
        previewImageView.fitHeightProperty().bind(previewImageAnchorPane.heightProperty());

        // Initialise the table view.
        // TODO load from storage; for now, we create a dummy list containing two games
        List<Game> games = new ArrayList<>();
        games.add(new Game(new Player("MvdR"), new Team(Umvc3Character.WOLVERINE, Umvc3Character.ZERO,
                Umvc3Character.DOCTOR_DOOM), new Player("mistermkl"), new Team(Umvc3Character.MORRIGAN,
                Umvc3Character.HAGGAR, Umvc3Character.SHUMA_GORATH)));
        games.add(new Game(new Player("Yipes"), new Team(Umvc3Character.NOVA, Umvc3Character.SPENCER,
                Umvc3Character.DOCTOR_STRANGE), new Player("PR Rog"), new Team(Umvc3Character.WOLVERINE,
                Umvc3Character.DOCTOR_DOOM, Umvc3Character.VERGIL)));

        replayTableView.setItems(FXCollections.observableList(games));

        log.info("Initialisation complete.");
    }
}
