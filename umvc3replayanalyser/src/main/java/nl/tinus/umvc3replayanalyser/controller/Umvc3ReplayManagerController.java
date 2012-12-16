package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Assist;
import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.model.Side;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.model.predicate.MatchReplayPredicate;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * Main controller class for this JavaFX application. Referenced from the corresponding fxml.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3ReplayManagerController implements ImportReplayListener {
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
    /** Character selection combo box. */
    @FXML
    private ComboBox<Umvc3Character> playerOneCharacterOneComboBox;
    /** Character selection combo box. */
    @FXML
    private ComboBox<Umvc3Character> playerOneCharacterTwoComboBox;
    /** Character selection combo box. */
    @FXML
    private ComboBox<Umvc3Character> playerOneCharacterThreeComboBox;
    /** Character selection combo box. */
    @FXML
    private ComboBox<Umvc3Character> playerTwoCharacterOneComboBox;
    /** Character selection combo box. */
    @FXML
    private ComboBox<Umvc3Character> playerTwoCharacterTwoComboBox;
    /** Character selection combo box. */
    @FXML
    private ComboBox<Umvc3Character> playerTwoCharacterThreeComboBox;
    /** Assist selection combo box. */
    @FXML
    private ComboBox<Assist> playerOneAssistOneComboBox;
    /** Assist selection combo box. */
    @FXML
    private ComboBox<Assist> playerOneAssistTwoComboBox;
    /** Assist selection combo box. */
    @FXML
    private ComboBox<Assist> playerOneAssistThreeComboBox;
    /** Assist selection combo box. */
    @FXML
    private ComboBox<Assist> playerTwoAssistOneComboBox;
    /** Assist selection combo box. */
    @FXML
    private ComboBox<Assist> playerTwoAssistTwoComboBox;
    /** Assist selection combo box. */
    @FXML
    private ComboBox<Assist> playerTwoAssistThreeComboBox;
    /** Check box indicating that the info filled in in the left column should only be matched to player one. */
    @FXML
    private CheckBox maintainPlayerOrderCheckBox;
    /** Check box indicating that the characters should only be matched in the given order. */
    @FXML
    private CheckBox maintainCharacterOrderCheckBox;
    /** Replays. */
    private List<Replay> replays;
    /** Indicates which character value each assist combo box depends on. */
    private Map<ObservableValue<Umvc3Character>, ComboBox<Assist>> assistComboBoxes;

    /** Initialisation method. */
    @FXML
    private void initialize() {
        log.info("Performing controller initialisation.");
        loadReplays();
        bindPreviewImageView();
        disableColumnSwapping();
        initTableView();
        initCharacterComboBoxValues();
        initAssistComboBoxes();
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
            log.debug("Replay selection changed, old value: " + oldValue + ", new value: " + newValue);
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

    /** Initialises the character combo box values. */
    private void initCharacterComboBoxValues() {
        for (ComboBox<Umvc3Character> comboBox : Arrays.asList(playerOneCharacterOneComboBox,
                playerOneCharacterTwoComboBox, playerOneCharacterThreeComboBox, playerTwoCharacterOneComboBox,
                playerTwoCharacterTwoComboBox, playerTwoCharacterThreeComboBox)) {
            comboBox.getItems().add(null);
            comboBox.getItems().addAll(Umvc3Character.values());
        }
    }
    
    /** Initialises the mapping of the assist combo boxes. */
    private void initAssistComboBoxes() {
        assistComboBoxes = new HashMap<>();
        assistComboBoxes.put(playerOneCharacterOneComboBox.valueProperty(), playerOneAssistOneComboBox);
        assistComboBoxes.put(playerOneCharacterTwoComboBox.valueProperty(), playerOneAssistTwoComboBox);
        assistComboBoxes.put(playerOneCharacterThreeComboBox.valueProperty(), playerOneAssistThreeComboBox);
        assistComboBoxes.put(playerTwoCharacterOneComboBox.valueProperty(), playerTwoAssistOneComboBox);
        assistComboBoxes.put(playerTwoCharacterTwoComboBox.valueProperty(), playerTwoAssistTwoComboBox);
        assistComboBoxes.put(playerTwoCharacterThreeComboBox.valueProperty(), playerTwoAssistThreeComboBox);
    }
    
    /** Adds listeners to the filter input fields. */
    private void initFilterListeners() {
        ChangeListener<Object> listener = new ChangeListener<Object>() {
            /** Used to prevent infinite recursion. */
            private boolean suspended = false;
            
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (!suspended) {
                    suspended = true;
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("Filter value changed. Old value: %s, new value: %s", oldValue, newValue));
                    }

                    updateAssistComboBox(observable);
                    updateReplayTable();
                    
                    suspended = false;
                }
            }
        };
        
        playerOneTextField.textProperty().addListener(listener);
        playerTwoTextField.textProperty().addListener(listener);
        playerOneCharacterOneComboBox.valueProperty().addListener(listener);
        playerOneCharacterTwoComboBox.valueProperty().addListener(listener);
        playerOneCharacterThreeComboBox.valueProperty().addListener(listener);
        playerTwoCharacterOneComboBox.valueProperty().addListener(listener);
        playerTwoCharacterTwoComboBox.valueProperty().addListener(listener);
        playerTwoCharacterThreeComboBox.valueProperty().addListener(listener);
        playerOneAssistOneComboBox.valueProperty().addListener(listener);
        playerOneAssistTwoComboBox.valueProperty().addListener(listener);
        playerOneAssistThreeComboBox.valueProperty().addListener(listener);
        playerTwoAssistOneComboBox.valueProperty().addListener(listener);
        playerTwoAssistTwoComboBox.valueProperty().addListener(listener);
        playerTwoAssistThreeComboBox.valueProperty().addListener(listener);
        maintainPlayerOrderCheckBox.selectedProperty().addListener(listener);
        maintainCharacterOrderCheckBox.selectedProperty().addListener(listener);
    }

    /**
     * If observable is a character, updates the corresponding assist combo box.
     * 
     * @param observable
     *            observable whose value has changed
     */
    private void updateAssistComboBox(ObservableValue<? extends Object> observable) {
        ComboBox<Assist> comboBox = this.assistComboBoxes.get(observable);
        if (comboBox != null) {
            // Character value has changed. Rebuild the contents of the combo box.
            Umvc3Character selectedCharacter = (Umvc3Character) observable.getValue();
            
            comboBox.getSelectionModel().clearSelection();
            
            comboBox.getItems().clear();
            if (selectedCharacter != null) {
                comboBox.getItems().add(null);
                for (AssistType type: AssistType.values()) {
                    comboBox.getItems().add(new Assist(type, selectedCharacter));
                }
            }
            comboBox.setDisable(selectedCharacter == null);
        }
    }
    
    /** Updates the replay table. */
    private void updateReplayTable() {
        log.debug("Updating replay table.");
        
        // Save the selected replay so we can reselect it later.
        Replay selectedReplay = replayTableView.getSelectionModel().getSelectedItem();
        
        // Construct the filter predicate
        Predicate<Replay> sideOnePredicate = new MatchReplayPredicate(playerOneTextField.getText(),
                playerOneCharacterOneComboBox.getValue(), playerOneCharacterTwoComboBox.getValue(),
                playerOneCharacterThreeComboBox.getValue(), getType(playerOneAssistOneComboBox.getValue()),
                getType(playerOneAssistTwoComboBox.getValue()), getType(playerOneAssistThreeComboBox.getValue()),
                maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_ONE);
        Predicate<Replay> sideTwoPredicate = new MatchReplayPredicate(playerTwoTextField.getText(),
                playerTwoCharacterOneComboBox.getValue(), playerTwoCharacterTwoComboBox.getValue(),
                playerTwoCharacterThreeComboBox.getValue(), getType(playerTwoAssistOneComboBox.getValue()),
                getType(playerTwoAssistTwoComboBox.getValue()), getType(playerTwoAssistThreeComboBox.getValue()),
                maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_TWO);
        Predicate<Replay> predicate = Predicates.and(sideOnePredicate, sideTwoPredicate);

        if (!maintainPlayerOrderCheckBox.isSelected()) {
            sideOnePredicate = new MatchReplayPredicate(playerTwoTextField.getText(),
                    playerTwoCharacterOneComboBox.getValue(), playerTwoCharacterTwoComboBox.getValue(),
                    playerTwoCharacterThreeComboBox.getValue(), getType(playerTwoAssistOneComboBox.getValue()),
                    getType(playerTwoAssistTwoComboBox.getValue()), getType(playerTwoAssistThreeComboBox.getValue()),
                    maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_ONE);
            sideTwoPredicate = new MatchReplayPredicate(playerOneTextField.getText(),
                    playerOneCharacterOneComboBox.getValue(), playerOneCharacterTwoComboBox.getValue(),
                    playerOneCharacterThreeComboBox.getValue(), getType(playerOneAssistOneComboBox.getValue()),
                    getType(playerOneAssistTwoComboBox.getValue()), getType(playerOneAssistThreeComboBox.getValue()),
                    maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_TWO);
            
            predicate = Predicates.or(predicate, Predicates.and(sideOnePredicate, sideTwoPredicate));
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Using predicate to filter replays: " + predicate);
        }
        
        Iterable<Replay> filteredReplays = Iterables.filter(replays, predicate);
        
        List<Replay> viewReplays = replayTableView.getItems();
        viewReplays.clear();
        for (Replay replay: filteredReplays) {
            viewReplays.add(replay);
        }
        
        if (log.isDebugEnabled()) {
            log.debug(String.format("Filtered replays. Displaying %s of %s replays.", ""
                    + viewReplays.size(), "" + this.replays.size()));
        }
        
        // Force a re-sort of the table.
        List<TableColumn<Replay, ?>> sortOrder = new ArrayList<>(replayTableView.getSortOrder());
        replayTableView.getSortOrder().setAll(sortOrder);
        
        // Attempt to reselect the originally selected replay.
        int newIndex = replayTableView.getItems().indexOf(selectedReplay);
        replayTableView.getSelectionModel().select(newIndex);
    }
    
    /**
     * Gets the assist type, while handling null values.
     * 
     * @param assist
     *            assist, may be null
     * @return assist type, or null if the given assist is null
     */
    private AssistType getType(Assist assist) {
        AssistType result;
        if (assist == null) {
            result = null;
        } else {
            result = assist.getType();
        }
        return result;
    }
    
    /** Action handler which exits the application. */
    @FXML
    private void handleExitAction(final ActionEvent event) {
        log.info("Close menu item selected; stopping the application.");
        Platform.exit();
    }
    
    /** Action handler which shows the about box. */
    @FXML
    private void handleAboutAction(final ActionEvent event) {
        log.info("About menu item selected. About box not implemented yet.");
        // TODO show an awesome about box!
    }
    
    /** Action handler to import replays. */
    @FXML
    private void handleImportAction(final ActionEvent event) {
        log.info("Import menu item selected.");
        
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Import Replays - Ultimate Marvel vs Capcom 3 Replay Manager");
        File selectedDirectory = chooser.showDialog(getApplicationWindow());
        
        log.info("Selected Directory: " + selectedDirectory + ".");
        
        if (selectedDirectory != null) {
            importReplays(selectedDirectory);      
        }
    }

    /**
     * Imports the replays from the given directory.
     * 
     * @param directory directory to be imported from
     */
    private void importReplays(File directory) {
        // TODO check directory does not contain the data directory
        // TODO move the FXML loading and initialisation to a class in the gui package
        // TODO prevent import popup from being shown multiple times
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/import-replay-popup.fxml"));
        fxmlLoader.setController(new ImportReplayPopupController(directory, Arrays.<ImportReplayListener>asList(this)));
        
        try {
            Parent root = (Parent) fxmlLoader.load();

            log.info("Fxml loaded, performing additional initialisation.");
            Stage stage = new Stage();
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
    
    /** @inheritDoc} */
    @Override
    public void replayImported(Replay replay) {
        log.info("Imported replay: " + replay);
        this.replays.add(replay);
        updateReplayTable();
    }
    
    /**
     * Returns the main window of this application.
     * 
     * @return window
     */
    private Window getApplicationWindow() {
        return this.replayTableView.getScene().getWindow();
    }
}
