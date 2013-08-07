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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.model.Assist;
import nl.mvdr.umvc3replayanalyser.model.AssistType;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Player;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import org.apache.commons.lang3.StringUtils;

/**
 * Controller for the edit replays view.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
// AllArgsConstructor intended for unit tests.
// In production code, use one of the other constructors and let JavaFX inject the @FXML-anotated fields.
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class EditReplayController {

    /**
     * Constructor.
     * 
     * @param okHandler
     *            event handler, called when the user activates the OK button
     */
    EditReplayController(ReplayDetailsEditedHandler okHandler) {
        this(null, okHandler);
    }
    
    /** Default contents for the form. May be null. */
    private final Game defaultContents;
    /** Event handler, called when the user activates the OK button. */
    @NonNull
    private final ReplayDetailsEditedHandler okHandler;

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
    /** OK button. */
    @FXML
    private Button okButton;

    /** Indicates which character value each assist combo box depends on. */
    private Map<ObservableValue<Umvc3Character>, ComboBox<Assist>> assistComboBoxes;

    /** Initialisation method. */
    // Default visibility for unit tests.
    @FXML
    void initialize() {
        log.info("Performing controller initialisation.");

        // Initialise character combo boxes.
        for (ComboBox<Umvc3Character> comboBox : Arrays.asList(playerOneCharacterOneComboBox,
                playerOneCharacterTwoComboBox, playerOneCharacterThreeComboBox, playerTwoCharacterOneComboBox,
                playerTwoCharacterTwoComboBox, playerTwoCharacterThreeComboBox)) {
            comboBox.getItems().addAll(Umvc3Character.values());
        }

        // Initialise assist combo boxes.
        assistComboBoxes = new HashMap<>();
        assistComboBoxes.put(playerOneCharacterOneComboBox.valueProperty(), playerOneAssistOneComboBox);
        assistComboBoxes.put(playerOneCharacterTwoComboBox.valueProperty(), playerOneAssistTwoComboBox);
        assistComboBoxes.put(playerOneCharacterThreeComboBox.valueProperty(), playerOneAssistThreeComboBox);
        assistComboBoxes.put(playerTwoCharacterOneComboBox.valueProperty(), playerTwoAssistOneComboBox);
        assistComboBoxes.put(playerTwoCharacterTwoComboBox.valueProperty(), playerTwoAssistTwoComboBox);
        assistComboBoxes.put(playerTwoCharacterThreeComboBox.valueProperty(), playerTwoAssistThreeComboBox);

        // Add a listener, so that whenever a character value is changed, the assist combo box is updated as well.
        ChangeListener<Umvc3Character> assistListener = new ChangeListener<Umvc3Character>() {
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends Umvc3Character> observable, Umvc3Character oldValue,
                    Umvc3Character newValue) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Character changed. Old value: %s, new value: %s", oldValue, newValue));
                }
                updateAssistComboBox(observable);
            }
        };
        playerOneCharacterOneComboBox.valueProperty().addListener(assistListener);
        playerOneCharacterTwoComboBox.valueProperty().addListener(assistListener);
        playerOneCharacterThreeComboBox.valueProperty().addListener(assistListener);
        playerTwoCharacterOneComboBox.valueProperty().addListener(assistListener);
        playerTwoCharacterTwoComboBox.valueProperty().addListener(assistListener);
        playerTwoCharacterThreeComboBox.valueProperty().addListener(assistListener);

        // Add another listener to ensure the OK button is enabled when the required fields have been filled in.
        ChangeListener<Object> okEnabledListener = new ChangeListener<Object>() {
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                if (log.isDebugEnabled()) {
                    log.debug(String.format("Observable value changed. Old value: %s, new value: %s", oldValue,
                            newValue));
                }
                okButton.setDisable(!isFilledIn());
            }
        };
        playerOneTextField.textProperty().addListener(okEnabledListener);
        playerTwoTextField.textProperty().addListener(okEnabledListener);
        playerOneCharacterOneComboBox.valueProperty().addListener(okEnabledListener);
        playerOneCharacterTwoComboBox.valueProperty().addListener(okEnabledListener);
        playerOneCharacterThreeComboBox.valueProperty().addListener(okEnabledListener);
        playerTwoCharacterOneComboBox.valueProperty().addListener(okEnabledListener);
        playerTwoCharacterTwoComboBox.valueProperty().addListener(okEnabledListener);
        playerTwoCharacterThreeComboBox.valueProperty().addListener(okEnabledListener);

        // Set the value of all fields based on the contents of defaultContents if available.
        // Do this after registering the listeners, so that when a character value is set, the assist combo box is
        // updated, and the status of the OK button is updated at the end.
        // Always update the assist value after the corresponding character value.
        if (defaultContents != null) {
            playerOneTextField.setText(defaultContents.getPlayerOne().getGamertag());
            playerTwoTextField.setText(defaultContents.getPlayerTwo().getGamertag());
            Team teamOne = defaultContents.getTeamOne();
            Team teamTwo = defaultContents.getTeamTwo();
            playerOneCharacterOneComboBox.setValue(teamOne.getFirstCharacter());
            playerOneAssistOneComboBox.setValue(teamOne.getFirstAssist());
            playerOneCharacterTwoComboBox.setValue(teamOne.getSecondCharacter());
            playerOneAssistTwoComboBox.setValue(teamOne.getSecondAssist());
            playerOneCharacterThreeComboBox.setValue(teamOne.getThirdCharacter());
            playerOneAssistThreeComboBox.setValue(teamOne.getThirdAssist());
            playerTwoCharacterOneComboBox.setValue(teamTwo.getFirstCharacter());
            playerTwoAssistOneComboBox.setValue(teamTwo.getFirstAssist());
            playerTwoCharacterTwoComboBox.setValue(teamTwo.getSecondCharacter());
            playerTwoAssistTwoComboBox.setValue(teamTwo.getSecondAssist());
            playerTwoCharacterThreeComboBox.setValue(teamTwo.getThirdCharacter());
            playerTwoAssistThreeComboBox.setValue(teamTwo.getThirdAssist());
        }

        log.info("Initialisation complete.");
    }

    /**
     * Updates the assist combo box corresponding to the given character value.
     * 
     * @param observable
     *            character observable whose value has changed
     */
    private void updateAssistComboBox(ObservableValue<? extends Umvc3Character> observable) {
        ComboBox<Assist> comboBox = this.assistComboBoxes.get(observable);
        if (comboBox == null) {
            throw new IllegalArgumentException("Unexpected observable: " + observable);
        }

        // Character value has changed. Rebuild the contents of the combo box.
        int index = comboBox.getSelectionModel().getSelectedIndex();
        comboBox.getItems().clear();
        comboBox.getItems().add(null);
        for (AssistType type : AssistType.values()) {
            comboBox.getItems().add(new Assist(type, observable.getValue()));
        }
        // Maintain selected index.
        comboBox.getSelectionModel().select(index);
        comboBox.setDisable(false);
    }

    /**
     * Converts the selection in the dialog box to a game and returns it.
     * 
     * Note that this method will throw a NullPointerException in case any required field values are null. This can be
     * checked using the isFilledIn() method.
     * 
     * @return new game instance based on the selection in the dialog box
     */
    private Game getGame() {
        Player playerOne = new Player(playerOneTextField.getText());
        Player playerTwo = new Player(playerTwoTextField.getText());
        Team teamOne = new Team(playerOneCharacterOneComboBox.getValue(),
                Assist.getType(playerOneAssistOneComboBox.getValue()),
                playerOneCharacterTwoComboBox.getValue(),
                Assist.getType(playerOneAssistTwoComboBox.getValue()),
                playerOneCharacterThreeComboBox.getValue(),
                Assist.getType(playerOneAssistThreeComboBox.getValue()));
        Team teamTwo = new Team(playerTwoCharacterOneComboBox.getValue(),
                Assist.getType(playerTwoAssistOneComboBox.getValue()),
                playerTwoCharacterTwoComboBox.getValue(),
                Assist.getType(playerTwoAssistTwoComboBox.getValue()),
                playerTwoCharacterThreeComboBox.getValue(),
                Assist.getType(playerTwoAssistThreeComboBox.getValue()));
        return new Game(playerOne, teamOne, playerTwo, teamTwo);
    }

    /**
     * Checks whether the required fields of the form have been filled in.
     * 
     * @return true if the required fields are filled in, false otherwise
     */
    private boolean isFilledIn() {
        return !StringUtils.isEmpty(playerOneTextField.getText()) && !StringUtils.isEmpty(playerTwoTextField.getText())
                && playerOneCharacterOneComboBox.getValue() != null && playerOneCharacterTwoComboBox.getValue() != null
                && playerOneCharacterThreeComboBox.getValue() != null
                && playerTwoCharacterOneComboBox.getValue() != null && playerTwoCharacterTwoComboBox.getValue() != null
                && playerTwoCharacterThreeComboBox.getValue() != null;
    }
    
    /** Action handler for the ok button. */
    @FXML
    private void handleOkAction() {
        log.info("OK button activated.");
        
        Game game = getGame();
        
        log.info("Game: " + game);
        
        okHandler.handleReplayDetailsEdited(game);
        
        getApplicationWindow().hide();
    }
    
    /** Action handler for the cancel button. */
    @FXML
    private void handleCancelAction() {
        log.info("Cancel button activated.");
        
        getApplicationWindow().hide();
    }
    
    /**
     * Returns the popup window.
     * 
     * @return window
     */
    private Window getApplicationWindow() {
        return this.okButton.getScene().getWindow();
    }
}
