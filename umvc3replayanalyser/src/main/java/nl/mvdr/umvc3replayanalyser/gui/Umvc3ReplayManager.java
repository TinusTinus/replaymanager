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
package nl.mvdr.umvc3replayanalyser.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import com.sun.javafx.runtime.VersionInfo;

/**
 * Main class, used to start the application. Defines the JavaFX user interface.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3ReplayManager extends Application {
    /** Title of the application. */
    private static final String TITLE = "Ultimate Marvel vs Capcom 3 Replay Manager";

    /**
     * Main method, starts the application.
     * 
     * @param args
     *            command line parameters, which are passed on to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        try {
            logVersionInfo();

            log.info("Starting application.");
            Parent root = FXMLLoader.load(getClass().getResource("/umvc3-replay-manager.fxml"));

            log.info("Fxml loaded, performing additional initialisation.");
            stage.setTitle(TITLE);
            stage.setScene(new Scene(root));
            stage.getIcons().add(Icons.get().getRandomPortrait());

            log.info("Showing UI.");
            stage.show();

            // Default size should also be the minimum size.
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());

            log.info("Application started.");
        } catch (Exception e) {
            log.error("Unable to start application.", e);
            ErrorMessagePopup.show(TITLE + " - failed to start up", TITLE
                    + " was unable to start due to an unexpected problem.", stage, e);
        }
    }

    /** Logs the version info. */
    private void logVersionInfo() {
        // TODO put some of the following version info in the about box?
        log.info("Classpath: " + System.getProperty("java.class.path"));
        log.info("Java vendor: " + System.getProperty("java.vendor"));
        log.info("Java version: " + System.getProperty("java.version"));
        log.info("OS name: " + System.getProperty("os.name"));
        log.info("OS version: " + System.getProperty("os.version"));
        log.info("OS architecture: " + System.getProperty("os.arch"));
        log.info("Using JavaFX runtime version: " + VersionInfo.getRuntimeVersion());
        if (log.isDebugEnabled()) {
            log.debug("Detailed JavaFX version info: ");
            log.debug("  Version: " + VersionInfo.getVersion());
            log.debug("  Runtime version: " + VersionInfo.getRuntimeVersion());
            log.debug("  Build timestamp: " + VersionInfo.getBuildTimestamp());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        log.info("Stopping application.");
    }
}
