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
package nl.tinus.umvc3replayanalyser.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class with helper methods to show an FXML popup.
 * 
 * @author Martijn van de Rijdt
 */
// private constructor to prevent utility class instantiation
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Popups {
    /**
     * Shows the import replay popup in a new stage.
     * 
     * @param controller
     *            controller for this popup window, typically an ImportReplayPopupController
     */
    public static void showImportReplaysPopup(Object controller) {
        showImportReplaysPopup(new Stage(), controller);
    }

    /**
     * Shows the import replay popup.
     * 
     * @param stage
     *            stage in which to show the user interface
     * @param controller
     *            controller for this popup window, typically an ImportReplayPopupController
     */
    public static void showImportReplaysPopup(Stage stage, Object controller) {
        show("/import-replay-popup.fxml", stage, controller, "Importing replays");
    }

    /**
     * Shows the about box replay popup.
     * 
     * @param controller
     *            controller for the about box, typically an AboutPopupController
     */
    public static void showAboutPopup(Object controller) {
        show("/about.fxml", new Stage(), controller, "About");
    }

    /**
     * Shows the edit replay details popup.
     * 
     * @param controller
     *            controller for the edit replay popup window, typically an EditReplayController
     */
    public static void showEditReplayPopup(Object controller) {
        show("/edit-replay.fxml", new Stage(), controller, "Edit replay details");
    }

    /**
     * Shows a popup.
     * 
     * @param fxmlFilename
     *            filename of the fxml file to be loaded
     * @param stage
     *            stage in which to show the user interface
     * @param controller
     *            controller for this popup window
     * @param title
     *            title for the popup window
     */
    static void show(String fxmlFilename, Stage stage, Object controller, String title) {
        log.info(String.format("Opening popup, title: %s, fxml file: %s", title, fxmlFilename));
        FXMLLoader fxmlLoader = new FXMLLoader(Popups.class.getResource(fxmlFilename));
        fxmlLoader.setController(controller);
        try {
            Parent root = (Parent) fxmlLoader.load();

            log.info("Fxml loaded, performing additional initialisation.");
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.getIcons().add(Icons.get().getRandomPortrait());

            log.info("Showing UI.");
            stage.show();

            // Default size should also be the minimum size.
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to parse FXML: " + fxmlFilename, e);
        }
    }
}
