package nl.tinus.umvc3replayanalyser.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Assist;
import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

/**
 * Controller for the edit replays view.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class EditReplayController {

    /** Default contents for the form. May be null. */
    private final Replay defaultContents;

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
    // TODO enable / disable the ok button when appropriate
    /** OK button. */
    @FXML
    private Button okButton;

    /** Indicates which character value each assist combo box depends on. */
    private Map<ObservableValue<Umvc3Character>, ComboBox<Assist>> assistComboBoxes;

    /** Initialisation method. */
    @FXML
    private void initialize() {
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
        ChangeListener<Umvc3Character> listener = new ChangeListener<Umvc3Character>() {
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
        playerOneCharacterOneComboBox.valueProperty().addListener(listener);
        playerOneCharacterTwoComboBox.valueProperty().addListener(listener);
        playerOneCharacterThreeComboBox.valueProperty().addListener(listener);
        playerTwoCharacterOneComboBox.valueProperty().addListener(listener);
        playerTwoCharacterTwoComboBox.valueProperty().addListener(listener);
        playerTwoCharacterThreeComboBox.valueProperty().addListener(listener);
        
        // TODO fill all fields based on the contents of defaultContents

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
        comboBox.getItems().clear();
        comboBox.getItems().add(null);
        for (AssistType type : AssistType.values()) {
            comboBox.getItems().add(new Assist(type, observable.getValue()));
        }
        comboBox.setDisable(false);
    }
}
