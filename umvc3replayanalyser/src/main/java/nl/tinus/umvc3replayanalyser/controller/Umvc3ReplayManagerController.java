package nl.tinus.umvc3replayanalyser.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Replay;
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
    private TableView<Replay> replayTableView;

    /** Initialisation method. */
    @FXML
    public void initialize() {
        log.info("Performing controller initialisation.");
        bindPreviewImageView();
        disableColumnSwapping();
        fillTableView();
        log.info("Initialisation complete.");
    }

    /** Binds the image size to the size of its parent. */
    private void bindPreviewImageView() {
        previewImageView.fitWidthProperty().bind(previewImageAnchorPane.widthProperty());
        previewImageView.fitHeightProperty().bind(previewImageAnchorPane.heightProperty());
    }

    /** Disables column swapping on the table view. */
    private void disableColumnSwapping() {
        // First make a copy of the columns.
        final ObservableList<TableColumn<Replay, ?>> columns = FXCollections.observableList(new ArrayList<>(
                replayTableView.getColumns()));
        // Now, whenever the list is changed, reset it to the original.
        replayTableView.getColumns().addListener(new ListChangeListener<TableColumn<Replay, ?>>() {
            /** Used to limit recursion to 1. */
            public boolean suspended;

            /** {@inheritDoc} */
            @Override
            public void onChanged(Change<? extends TableColumn<Replay, ?>> change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    replayTableView.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
    }

    /** Fills the tableview with data. */
    private void fillTableView() {
        // Initialise the table view.
        // TODO load from storage; for now, we create a dummy list containing two games
        List<Replay> replays = new ArrayList<>();
        replays.add(new Replay(new Date(), new Game(new Player("MvdR"), new Team(Umvc3Character.WOLVERINE,
                Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM), new Player("mistermkl"), new Team(
                Umvc3Character.MORRIGAN, Umvc3Character.HAGGAR, Umvc3Character.SHUMA_GORATH)), "/badhyper-vs-MvdR.mp4",
                "/vs.png"));
        replays.add(new Replay(new Date(), new Game(new Player("Yipes"), new Team(Umvc3Character.NOVA,
                Umvc3Character.SPENCER, Umvc3Character.DOCTOR_STRANGE), new Player("PR Rog"), new Team(
                Umvc3Character.WOLVERINE, Umvc3Character.DOCTOR_DOOM, Umvc3Character.VERGIL)), "/badhyper-vs-MvdR.mp4",
                "/vswithoutnames.png"));

        replayTableView.setItems(FXCollections.observableList(replays));
    }
}
