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

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import nl.mvdr.umvc3replayanalyser.model.Assist;
import nl.mvdr.umvc3replayanalyser.model.AssistType;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Player;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link EditReplayController}.
 * 
 * @author Martijn van de Rijdt
 */
public class EditReplayControllerTest {
    /** Setup method. */
    @Before
    public void setUp() {
        // Force JavaFX graphics initialisation; this is required by the ComboBox constructor.
        // When running the actual application this is performed by Application.launch().
        new JFXPanel();
    }
    
    /**
     * Tests what happens to the value of the assist combobox when the value of the corresponding character combobox is
     * changed.
     */
    @Test
    public void testMaintainAssistTypeSelection() {
        // Set up the controller.
        Team teamOne = new Team(Umvc3Character.AKUMA, AssistType.ALPHA, Umvc3Character.ARTHUR, null,
                Umvc3Character.C_VIPER, null);
        Team teamTwo = new Team(Umvc3Character.CAPTAIN_AMERICA, Umvc3Character.CHRIS, Umvc3Character.CHUN_LI);
        Game defaultContents = new Game(new Player("one"), teamOne, new Player("two"), teamTwo);

        // ComboBoxes for player 1's first character and assist.
        ComboBox<Umvc3Character> characterComboBox = new ComboBox<>();
        ComboBox<Assist> assistComboBox = new ComboBox<>();

        EditReplayController controller = new EditReplayController(defaultContents, new LogReplayDetailsHandler(),
                new TextField(), new TextField(), characterComboBox, new ComboBox<Umvc3Character>(),
                new ComboBox<Umvc3Character>(), new ComboBox<Umvc3Character>(), new ComboBox<Umvc3Character>(),
                new ComboBox<Umvc3Character>(), assistComboBox, new ComboBox<Assist>(),
                new ComboBox<Assist>(), new ComboBox<Assist>(), new ComboBox<Assist>(), new ComboBox<Assist>(),
                new Button(), null);
        controller.initialize();

        // Check that the first character and first assist have been initialised correctly.
        Assert.assertEquals(Umvc3Character.AKUMA, characterComboBox.getValue());
        Assert.assertEquals(4, assistComboBox.getItems().size());
        Assert.assertNull(assistComboBox.getItems().get(0));
        Assert.assertEquals(Umvc3Character.AKUMA, assistComboBox.getItems().get(1).getCharacter());
        Assert.assertEquals(AssistType.ALPHA, assistComboBox.getItems().get(1).getType());
        Assert.assertEquals(Umvc3Character.AKUMA, assistComboBox.getItems().get(2).getCharacter());
        Assert.assertEquals(AssistType.BETA, assistComboBox.getItems().get(2).getType());
        Assert.assertEquals(Umvc3Character.AKUMA, assistComboBox.getItems().get(3).getCharacter());
        Assert.assertEquals(AssistType.GAMMA, assistComboBox.getItems().get(3).getType());
        Assert.assertEquals(assistComboBox.getItems().get(1), assistComboBox.getValue());

        // Change the first character to Ammy.
        characterComboBox.setValue(Umvc3Character.AMATERASU);

        // Check that the first character and first assist have been updated correctly, and the assist type
        // selection remains intact.
        Assert.assertEquals(Umvc3Character.AMATERASU, characterComboBox.getValue());
        Assert.assertEquals(4, assistComboBox.getItems().size());
        Assert.assertNull(assistComboBox.getItems().get(0));
        Assert.assertEquals(Umvc3Character.AMATERASU, assistComboBox.getItems().get(1).getCharacter());
        Assert.assertEquals(AssistType.ALPHA, assistComboBox.getItems().get(1).getType());
        Assert.assertEquals(Umvc3Character.AMATERASU, assistComboBox.getItems().get(2).getCharacter());
        Assert.assertEquals(AssistType.BETA, assistComboBox.getItems().get(2).getType());
        Assert.assertEquals(Umvc3Character.AMATERASU, assistComboBox.getItems().get(3).getCharacter());
        Assert.assertEquals(AssistType.GAMMA, assistComboBox.getItems().get(3).getType());
        Assert.assertEquals(assistComboBox.getItems().get(1), assistComboBox.getValue());
    }
}
