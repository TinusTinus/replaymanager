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
package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.config.Configuration;
import nl.tinus.umvc3replayanalyser.config.PropertiesConfiguration;
import nl.tinus.umvc3replayanalyser.gui.Popups;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyserImpl;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.ocr.OCREngine;
import nl.tinus.umvc3replayanalyser.ocr.TesseractOCREngine;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalyser;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalyserImpl;

/**
 * Main class which lets us test the import replay popup as a standalone application.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class ImportReplayPopupMain extends Application {

    /** Directory from which replays are loaded. */
    private static final String REPLAY_DIRECTORY = "src/test/resources";

    /**
     * Main method.
     * 
     * @param args
     *            command line parameters, which are passed on to JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) throws IOException {
        log.info("Starting application.");

        final ArrayList<Replay> replays = new ArrayList<Replay>();
        
        SimpleBooleanProperty working = new SimpleBooleanProperty();
        working.addListener(new ChangeListener<Boolean>() {
            /** {@inheritDoc} */
            @Override
            public void changed(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) {
                log.info("working changed from " + oldValue + " to " + newValue + "; " + replays.size() + " replays: "
                        + replays);
            }
        });

        Configuration configuration = new PropertiesConfiguration();
        OCREngine ocrEngine = new TesseractOCREngine(configuration);
        VersusScreenAnalyser versusScreenAnalyser = new VersusScreenAnalyserImpl(ocrEngine);
        ReplayAnalyser replayAnalyser = new ReplayAnalyserImpl(versusScreenAnalyser);
        ReplaySaver replaySaver = new ReplaySaver(configuration);
        ImportReplayTask task = new ImportReplayTask(new File(REPLAY_DIRECTORY), replays, replayAnalyser, replaySaver);
        ImportReplayPopupController controller = new ImportReplayPopupController(task, working, "Replay Import Thread");

        Popups.showImportReplaysPopup(stage, controller);

        log.info("Application started.");
    }
    
    /** {@inheritDoc} */
    @Override
    public void stop() {
        log.info("Stopping application.");
    }
}
