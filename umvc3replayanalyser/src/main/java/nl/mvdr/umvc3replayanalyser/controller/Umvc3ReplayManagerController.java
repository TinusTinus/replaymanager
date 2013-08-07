/*
 * Copyright 2012, 2013 Martijn van de Rijdt 
 * 
 * This file is part of the Ultimate Marvel vs Capcom 3 Replay Manager.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with the Ultimate Marvel vs Capcom 3
 * Replay Manager. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mvdr.umvc3replayanalyser.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.config.Configuration;
import nl.mvdr.umvc3replayanalyser.config.PropertiesConfiguration;
import nl.mvdr.umvc3replayanalyser.gui.ErrorMessagePopup;
import nl.mvdr.umvc3replayanalyser.gui.Icons;
import nl.mvdr.umvc3replayanalyser.gui.Popups;
import nl.mvdr.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.mvdr.umvc3replayanalyser.image.VersusScreenAnalyserImpl;
import nl.mvdr.umvc3replayanalyser.model.Assist;
import nl.mvdr.umvc3replayanalyser.model.AssistType;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Replay;
import nl.mvdr.umvc3replayanalyser.model.Side;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;
import nl.mvdr.umvc3replayanalyser.model.predicate.MatchReplayPredicate;
import nl.mvdr.umvc3replayanalyser.ocr.OCREngine;
import nl.mvdr.umvc3replayanalyser.ocr.TesseractOCREngine;
import nl.mvdr.umvc3replayanalyser.video.ReplayAnalyser;
import nl.mvdr.umvc3replayanalyser.video.ReplayAnalyserImpl;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.map.ObjectMapper;

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
    /** Menu item for importing new replays. */
    @FXML
    private MenuItem importMenuItem;
    /** Player name label. */
    @FXML
    private Label playerOneLabel;
    /** Player name label. */
    @FXML
    private Label playerTwoLabel;
    /** Character portrait. */
    @FXML
    private ImageView playerOneCharacterOneImageView;
    /** Character portrait. */
    @FXML
    private ImageView playerOneCharacterTwoImageView;
    /** Character portrait. */
    @FXML
    private ImageView playerOneCharacterThreeImageView;
    /** Character portrait. */
    @FXML
    private ImageView playerTwoCharacterOneImageView;
    /** Character portrait. */
    @FXML
    private ImageView playerTwoCharacterTwoImageView;
    /** Character portrait. */
    @FXML
    private ImageView playerTwoCharacterThreeImageView;
    /** Character label. */
    @FXML
    private Label playerOneCharacterOneLabel;
    /** Character label. */
    @FXML
    private Label playerOneCharacterTwoLabel;
    /** Character label. */
    @FXML
    private Label playerOneCharacterThreeLabel;
    /** Character label. */
    @FXML
    private Label playerTwoCharacterOneLabel;
    /** Character label. */
    @FXML
    private Label playerTwoCharacterTwoLabel;
    /** Character label. */
    @FXML
    private Label playerTwoCharacterThreeLabel;
    /** Assist label. */
    @FXML
    private Label playerOneAssistOneLabel;
    /** Assist label. */
    @FXML
    private Label playerOneAssistTwoLabel;
    /** Assist label. */
    @FXML
    private Label playerOneAssistThreeLabel;
    /** Assist label. */
    @FXML
    private Label playerTwoAssistOneLabel;
    /** Assist label. */
    @FXML
    private Label playerTwoAssistTwoLabel;
    /** Assist label. */
    @FXML
    private Label playerTwoAssistThreeLabel;
    /** Button used to open the replay video. */
    @FXML
    private Button openVideoButton;
    /** Button used to edit replay details. */
    @FXML
    private Button editReplayButton;

    /** Application configuration. */
    private Configuration configuration;
    /** Replay analyser. */
    private ReplayAnalyser replayAnalyser;
    /** Replays. */
    private ObservableList<Replay> replays;
    /** Indicates which character value each assist combo box depends on. */
    private Map<ObservableValue<Umvc3Character>, ComboBox<Assist>> assistComboBoxes;
    /** Replay saver. */
    private ReplaySaver replaySaver;

    /** Initialisation method. */
    @FXML
    private void initialize() {
        log.info("Performing controller initialisation.");
        loadConfiguration();
        initReplayAnalyser();
        loadReplays();
        bindPreviewImageView();
        disableColumnSwapping();
        initTableView();
        handleSelectedReplayChanged(null);
        initCharacterComboBoxValues();
        initAssistComboBoxes();
        initFilterListeners();
        log.info("Initialisation complete.");
    }

    /** Loads the configuration. */
    private void loadConfiguration() {
        if (this.configuration != null) {
            throw new IllegalStateException("Configuration already loaded.");
        }
        this.configuration = new PropertiesConfiguration();
    }

    /** Initialises the replay analyser. */
    private void initReplayAnalyser() {
        OCREngine ocrEngine = new TesseractOCREngine(this.configuration);
        VersusScreenAnalyser versusScreenAnalyser = new VersusScreenAnalyserImpl(ocrEngine);
        this.replayAnalyser = new ReplayAnalyserImpl(versusScreenAnalyser);
        this.replaySaver = new ReplaySaver(this.configuration);
    }
    
    /** Loads the replays from storage. */
    private void loadReplays() {
        if (replays != null) {
            throw new IllegalStateException("Replays already loaded: " + replays);
        }

        replays = FXCollections.observableList(new ArrayList<Replay>());

        File dataDirectory = new File(this.configuration.getDataDirectoryPath());
        if (!dataDirectory.exists()) {
            throw new IllegalStateException("Not an existing path: " + dataDirectory + ". Check your configuration.");
        }
        if (!dataDirectory.isDirectory()) {
            throw new IllegalStateException("Not a directory: " + dataDirectory + ". Check your configuration.");
        }

        ObjectMapper mapper = new ObjectMapper();
        for (File file : dataDirectory.listFiles()) {
            if (file.getName().endsWith(".replay")) {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Attempting to load: " + file);
                    }
                    Replay replay = mapper.readValue(file, Replay.class);
                    log.info("Loaded from " + file + ": " + replay.getGame());
                    replays.add(replay);
                } catch (IOException e) {
                    log.warn("Failed to import replay from " + file, e);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Skipping file: " + file);
                }
            }
        }

        replays.addListener(new ListChangeListener<Replay>() {
            /** {@inheritDoc} */
            @Override
            public void onChanged(Change<? extends Replay> change) {
                if (log.isDebugEnabled()) {
                    log.debug("Replay list changed: " + change);
                }
                updateReplayTable();
            }
        });
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
                handleSelectedReplayChanged(newValue);
            }
        });
    }

    /**
     * Handles the case where the selection in the replay table changes.
     * 
     * @param newValue
     *            new value
     */
    private void handleSelectedReplayChanged(Replay newValue) {
        if (log.isDebugEnabled()) {
            log.debug("Replay selection changed, new value: " + newValue);
        }
        // Update the preview image.
        if (newValue != null) {
            // A new replay was selected.
            // Update the preview image.
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

            // Also update the replay details view.
            // player names
            playerOneLabel.setText(newValue.getGame().getPlayerOne().getGamertag());
            playerTwoLabel.setText(newValue.getGame().getPlayerTwo().getGamertag());
            // character names
            playerOneCharacterOneLabel.setText(newValue.getGame().getTeamOne().getFirstCharacter().getName());
            playerOneCharacterTwoLabel.setText(newValue.getGame().getTeamOne().getSecondCharacter().getName());
            playerOneCharacterThreeLabel.setText(newValue.getGame().getTeamOne().getThirdCharacter().getName());
            playerTwoCharacterOneLabel.setText(newValue.getGame().getTeamTwo().getFirstCharacter().getName());
            playerTwoCharacterTwoLabel.setText(newValue.getGame().getTeamTwo().getSecondCharacter().getName());
            playerTwoCharacterThreeLabel.setText(newValue.getGame().getTeamTwo().getThirdCharacter().getName());
            // portraits
            playerOneCharacterOneImageView.setImage(Icons.get().getPortrait(
                    newValue.getGame().getTeamOne().getFirstCharacter()));
            playerOneCharacterTwoImageView.setImage(Icons.get().getPortrait(
                    newValue.getGame().getTeamOne().getSecondCharacter()));
            playerOneCharacterThreeImageView.setImage(Icons.get().getPortrait(
                    newValue.getGame().getTeamOne().getThirdCharacter()));
            playerTwoCharacterOneImageView.setImage(Icons.get().getPortrait(
                    newValue.getGame().getTeamTwo().getFirstCharacter()));
            playerTwoCharacterTwoImageView.setImage(Icons.get().getPortrait(
                    newValue.getGame().getTeamTwo().getSecondCharacter()));
            playerTwoCharacterThreeImageView.setImage(Icons.get().getPortrait(
                    newValue.getGame().getTeamTwo().getThirdCharacter()));
            // assists
            playerOneAssistOneLabel.setText(getAssistText(newValue.getGame().getTeamOne().getFirstAssist()));
            playerOneAssistTwoLabel.setText(getAssistText(newValue.getGame().getTeamOne().getSecondAssist()));
            playerOneAssistThreeLabel.setText(getAssistText(newValue.getGame().getTeamOne().getThirdAssist()));
            playerTwoAssistOneLabel.setText(getAssistText(newValue.getGame().getTeamTwo().getFirstAssist()));
            playerTwoAssistTwoLabel.setText(getAssistText(newValue.getGame().getTeamTwo().getSecondAssist()));
            playerTwoAssistThreeLabel.setText(getAssistText(newValue.getGame().getTeamTwo().getThirdAssist()));
            // button
            openVideoButton.setDisable(false);
            editReplayButton.setDisable(false);
        } else {
            // Item was deselected.
            previewImageView.setImage(defaultPreviewImage);
            // player names
            playerOneLabel.setText("Player one");
            playerTwoLabel.setText("Player two");
            // character names
            playerOneCharacterOneLabel.setText("Point character");
            playerOneCharacterTwoLabel.setText("Second character");
            playerOneCharacterThreeLabel.setText("Anchor character");
            playerTwoCharacterOneLabel.setText("Point character");
            playerTwoCharacterTwoLabel.setText("Second character");
            playerTwoCharacterThreeLabel.setText("Anchor character");
            // portraits
            playerOneCharacterOneImageView.setImage(Icons.get().getGameIcon());
            playerOneCharacterTwoImageView.setImage(Icons.get().getGameIcon());
            playerOneCharacterThreeImageView.setImage(Icons.get().getGameIcon());
            playerTwoCharacterOneImageView.setImage(Icons.get().getGameIcon());
            playerTwoCharacterTwoImageView.setImage(Icons.get().getGameIcon());
            playerTwoCharacterThreeImageView.setImage(Icons.get().getGameIcon());
            // assists
            playerOneAssistOneLabel.setText("");
            playerOneAssistTwoLabel.setText("");
            playerOneAssistThreeLabel.setText("");
            playerTwoAssistOneLabel.setText("");
            playerTwoAssistTwoLabel.setText("");
            playerTwoAssistThreeLabel.setText("");
            // button
            openVideoButton.setDisable(true);
            editReplayButton.setDisable(true);
        }
    }

    /**
     * Given an assist, returns the textual representation of the assist for the details view.
     * 
     * @param assist
     *            assist to be represented; may be null
     * @return string representation of the given assist
     */
    private String getAssistText(Assist assist) {
        String result;
        if (assist != null) {
            result = "(" + assist.getName() + ")";
        } else {
            result = "";
        }
        return result;
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
                        log.debug(String.format("Filter value changed. Old value: %s, new value: %s", oldValue,
                                newValue));
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
                for (AssistType type : AssistType.values()) {
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
                playerOneCharacterOneComboBox.getValue(), Assist.getType(playerOneAssistOneComboBox.getValue()),
                playerOneCharacterTwoComboBox.getValue(), Assist.getType(playerOneAssistTwoComboBox.getValue()),
                playerOneCharacterThreeComboBox.getValue(), Assist.getType(playerOneAssistThreeComboBox.getValue()),
                maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_ONE);
        Predicate<Replay> sideTwoPredicate = new MatchReplayPredicate(playerTwoTextField.getText(),
                playerTwoCharacterOneComboBox.getValue(), Assist.getType(playerTwoAssistOneComboBox.getValue()),
                playerTwoCharacterTwoComboBox.getValue(), Assist.getType(playerTwoAssistTwoComboBox.getValue()),
                playerTwoCharacterThreeComboBox.getValue(), Assist.getType(playerTwoAssistThreeComboBox.getValue()),
                maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_TWO);
        Predicate<Replay> predicate = Predicates.and(sideOnePredicate, sideTwoPredicate);

        if (!maintainPlayerOrderCheckBox.isSelected()) {
            sideOnePredicate = new MatchReplayPredicate(playerTwoTextField.getText(),
                    playerTwoCharacterOneComboBox.getValue(), Assist.getType(playerTwoAssistOneComboBox.getValue()),
                    playerTwoCharacterTwoComboBox.getValue(), Assist.getType(playerTwoAssistTwoComboBox.getValue()),
                    playerTwoCharacterThreeComboBox.getValue(),
                    Assist.getType(playerTwoAssistThreeComboBox.getValue()),
                    maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_ONE);
            sideTwoPredicate = new MatchReplayPredicate(playerOneTextField.getText(),
                    playerOneCharacterOneComboBox.getValue(), Assist.getType(playerOneAssistOneComboBox.getValue()),
                    playerOneCharacterTwoComboBox.getValue(), Assist.getType(playerOneAssistTwoComboBox.getValue()),
                    playerOneCharacterThreeComboBox.getValue(),
                    Assist.getType(playerOneAssistThreeComboBox.getValue()),
                    maintainCharacterOrderCheckBox.isSelected(), Side.PLAYER_TWO);
            predicate = Predicates.or(predicate, Predicates.and(sideOnePredicate, sideTwoPredicate));
        }

        if (log.isDebugEnabled()) {
            log.debug("Using predicate to filter replays: " + predicate);
        }

        Iterable<Replay> filteredReplays = Iterables.filter(replays, predicate);

        List<Replay> viewReplays = replayTableView.getItems();
        viewReplays.clear();
        for (Replay replay : filteredReplays) {
            viewReplays.add(replay);
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Filtered replays. Displaying %s of %s replays.", "" + viewReplays.size(), ""
                    + this.replays.size()));
        }

        // Force a re-sort of the table.
        List<TableColumn<Replay, ?>> sortOrder = new ArrayList<>(replayTableView.getSortOrder());
        replayTableView.getSortOrder().setAll(sortOrder);

        // Attempt to reselect the originally selected replay.
        int newIndex = replayTableView.getItems().indexOf(selectedReplay);
        replayTableView.getSelectionModel().select(newIndex);
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
        log.info("About menu item selected.");
        Popups.showAboutPopup(new AboutPopupController());
    }

    /** Action handler to import replays. */
    @FXML
    private void handleImportAction(final ActionEvent event) {
        log.info("Import New Replays menu item selected.");

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Import Replays - Ultimate Marvel vs Capcom 3 Replay Manager");
        File selectedDirectory = chooser.showDialog(getApplicationWindow());

        log.info("Selected directory: " + selectedDirectory + ".");

        if (selectedDirectory != null) {
            importReplays(selectedDirectory);
        }
    }
    
    /**
     * Imports the replays from the given directory.
     * 
     * @param directory
     *            directory to be imported from
     */
    private void importReplays(File directory) {
        ImportReplayTask task = new ImportReplayTask(directory, this.replays, this.replayAnalyser, this.replaySaver);
        ImportReplayPopupController controller = new ImportReplayPopupController(task,
                this.importMenuItem.disableProperty(), "Replay Import Thread");
        Popups.showImportReplaysPopup(controller);
    }
    
    /** Action handler to import a single replay manually. */
    @FXML
    private void handleAddReplayAction(final ActionEvent event) {
        log.info("Add Replay Manually menu item selected.");

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Add Replay - Ultimate Marvel vs Capcom 3 Replay Manager");
        final File selectedFile = chooser.showOpenDialog(getApplicationWindow());

        log.info("Selected file: " + selectedFile + ".");

        if (selectedFile != null) {
            EditReplayController controller = new EditReplayController(new ReplayDetailsEditedHandler() {
                /** {@inheritDoc} */
                @Override
                public void handleReplayDetailsEdited(Game game) {
                    addReplay(selectedFile, game);
                }
            });
            Popups.showEditReplayPopup(controller);
        }
    }

    /**
     * Adds a replay for the given video file, with the data from the given game.
     * 
     * @param file
     *            original video file
     * @param game
     *            game
     */
    private void addReplay(File file, Game game) {
        try {
            Replay replay = replaySaver.saveReplay(file, game, "ultimate-marvel-vs-capcom-3.jpg");
            replays.add(replay);
        } catch (IOException e) {
            log.error(String.format("Unable to save replay; video file: %s, game: %s", file, game), e);
            ErrorMessagePopup.show("Unable to save replay.", "The replay could not be saved.", e);
        }
    }
    
    /** Handles the case where the user clicks the Open video button. */
    @FXML
    private void handleOpenVideoAction() {
        log.info("Open video button clicked.");

        Replay selectedReplay = replayTableView.getSelectionModel().getSelectedItem();
        if (selectedReplay == null) {
            throw new IllegalStateException("No replay selected; open video button should have been disabled!");
        }

        if (Desktop.isDesktopSupported()) {
            String videoFilePath = FilenameUtils.normalize(this.configuration.getDataDirectory().getAbsolutePath()
                    + FileUtils.SEPARATOR + selectedReplay.getVideoLocation());
            log.info("Playing video: " + videoFilePath);
            File videoFile = new File(videoFilePath);
            try {
                Desktop.getDesktop().open(videoFile);
            } catch (IOException | IllegalArgumentException e) {
                log.error("Unable to play video for replay: " + selectedReplay, e);
                // Show an error message to the user.
                String errorMessage = "Unable to play video for game: "
                        + selectedReplay.getGame().getDescription(false);
                if (e.getMessage() != null) {
                    errorMessage = errorMessage + " " + e.getMessage();
                }
                ErrorMessagePopup.show("Unable to play video", errorMessage, e);
            }
        } else {
            log.error("Unable to play video, desktop not supported!");
            // Show an error message to the user.
            ErrorMessagePopup.show("Unable to play video", "Unable to play video files.", null);
        }
    }
    
    /** Handles the case when the user clicks the Edit replay button. */
    @FXML
    private void handleEditReplayAction() {
        log.info("Edit replay button clicked.");
        
        final Replay selectedReplay = replayTableView.getSelectionModel().getSelectedItem();
        if (selectedReplay == null) {
            throw new IllegalStateException("No replay selected; edit replay button should have been disabled!");
        }
        
        EditReplayController controller = new EditReplayController(selectedReplay.getGame(),
                new ReplayDetailsEditedHandler() {
                    /** {@inheritDoc} */
                    @Override
                    public void handleReplayDetailsEdited(Game game) {
                        editReplay(selectedReplay, game);
                    }
                });
        Popups.showEditReplayPopup(controller);
    }

    /**
     * Edits a replay.
     * 
     * @param selectedReplay
     *            replay to be edited
     * @param game
     *            game, containing the new replay details to be used
     */
    private void editReplay(Replay selectedReplay, Game game) {
        try {
            Replay newReplay = replaySaver.editReplay(selectedReplay, game);

            replays.remove(selectedReplay);
            replays.add(newReplay);

            if (replayTableView.getSelectionModel().getSelectedIndex() < 0) {
                // No current selection. Select the newly added replay.
                int newIndex = replayTableView.getItems().indexOf(newReplay);
                replayTableView.getSelectionModel().select(newIndex);
            }
        } catch (IOException e) {
            log.error(String.format("Unable to edit replay details for replay %s", selectedReplay, game), e);
            ErrorMessagePopup.show("Unable to save replay.", "The replay could not be saved.", e);
        }
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
