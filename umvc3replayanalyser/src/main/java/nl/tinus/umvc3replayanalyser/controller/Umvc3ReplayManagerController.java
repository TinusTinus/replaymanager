package nl.tinus.umvc3replayanalyser.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.model.predicate.GamertagPrefixReplayPredicate;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * Main controller class for this JavaFX application. Referenced from the corresponding fxml.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3ReplayManagerController {
    /** Replays. */
    private List<Replay> replays;
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
    /** First text field for player name. */
    @FXML
    private TextField playerOneTextField;
    /** Second text field for player name. */
    @FXML
    private TextField playerTwoTextField;

    /** Initialisation method. */
    @FXML
    private void initialize() {
        log.info("Performing controller initialisation.");
        loadReplays();
        bindPreviewImageView();
        disableColumnSwapping();
        initTableView();
        initFilterListeners();
        log.info("Initialisation complete.");
    }
    
    /** Loads the replays from storage. */
    private void loadReplays() {
        if (replays != null) {
            throw new IllegalStateException("Replays already loaded.");
        }
        
        // TODO load from storage
        // For now, we create a dummy list containing some games.
        replays = new ArrayList<>();
        replays.add(new Replay(new Date(System.currentTimeMillis() + 1000), new Game(new Player("MvdR"), new Team(
                Umvc3Character.WOLVERINE, Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM), new Player("mistermkl"),
                new Team(Umvc3Character.MORRIGAN, Umvc3Character.HAGGAR, Umvc3Character.SHUMA_GORATH)),
                "/badhyper-vs-MvdR.mp4", "/vs.png"));
        replays.add(new Replay(new Date(), new Game(new Player("Yipes"), new Team(Umvc3Character.NOVA,
                Umvc3Character.SPENCER, Umvc3Character.DOCTOR_STRANGE), new Player("PR Rog"), new Team(
                Umvc3Character.WOLVERINE, Umvc3Character.DOCTOR_DOOM, Umvc3Character.VERGIL)), "/badhyper-vs-MvdR.mp4",
                "/vswithoutnames.png"));
    }

    /** Binds the image size to the size of its parent. */
    private void bindPreviewImageView() {
        previewImageView.fitWidthProperty().bind(previewImageAnchorPane.widthProperty());
        previewImageView.fitHeightProperty().bind(previewImageAnchorPane.heightProperty());
    }

    /** Disables column swapping on the table view. */
    // As far as I know there is no easy way to do this directly in the FXML (yet?), so we do this using a Java hack.
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

    /** Intialises the table view. */
    private void initTableView() {
        List<Replay> replaysView = new ArrayList<>(replays);
        replayTableView.setItems(FXCollections.observableList(replaysView));

        // Set default sort order.
        replayTableView.getSortOrder().add(replayTableView.getColumns().get(0));

        // Set listener for item selection.
        replayTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Replay>() {
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends Replay> observable, Replay oldValue, Replay newValue) {
                handleSelectedReplayChanged(oldValue, newValue);
            }
        });
    }

    /**
     * Handles the case where the selection in the replay table changes.
     * 
     * @param oldValue
     *            old value
     * @param newValue
     *            new value
     */
    private void handleSelectedReplayChanged(Replay oldValue, Replay newValue) {
        if (log.isDebugEnabled()) {
            log.debug("Selection changed, old value: " + oldValue + ", new value: " + newValue);
        }

        // Update the preview image.
        if (newValue != null) {
            // A new replay was selected.
            
            // For now, newValue.previewImageLocation contains a reference to the absolute path of the image.
            // This will eventually be a path relative to the application's data directory.
            // TODO add data directory to create a full absolute path
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Loading image: " + newValue.getPreviewImageLocation());
                }
                Image previewImage = new Image(newValue.getPreviewImageLocation());
                previewImageView.setImage(previewImage);
            } catch (IllegalArgumentException e) {
                log.warn("Unable to load image: " + newValue.getPreviewImageLocation(), e);
                previewImageView.setImage(defaultPreviewImage);
            }
        } else {
            // Item was deselected.
            previewImageView.setImage(defaultPreviewImage);
        }

        // TODO also update contents of the replay details pane
    }
    
    /** Adds listeners to the filter input fields. */
    private void initFilterListeners() {
        ChangeListener<String> listener = new ChangeListener<String>() {
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                handleFiltersChanged();
            }
        };
        playerOneTextField.textProperty().addListener(listener);
        playerTwoTextField.textProperty().addListener(listener);
    }
    
    /** Handles the case where any of the inputs have changed in the filters panel. */
    private void handleFiltersChanged() {
        Collection<Predicate<Replay>> components = new HashSet<Predicate<Replay>>();
        // TODO process side
        components.add(new GamertagPrefixReplayPredicate(playerOneTextField.getText(), null));
        components.add(new GamertagPrefixReplayPredicate(playerTwoTextField.getText(), null));
        
        Predicate<Replay> predicate = Predicates.and(components);
        
        Iterable<Replay> filteredReplays = Iterables.filter(replays, predicate);
        
        List<Replay> viewReplays = replayTableView.getItems();
        viewReplays.clear();
        for (Replay replay: filteredReplays) {
            viewReplays.add(replay);
        }
    }
}
