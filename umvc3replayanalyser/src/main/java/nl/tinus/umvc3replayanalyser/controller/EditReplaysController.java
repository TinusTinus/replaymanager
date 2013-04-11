package nl.tinus.umvc3replayanalyser.controller;

import javafx.fxml.FXML;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the edit replays view.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class EditReplaysController {
    
    /** Initialisation method. */
    @FXML
    private void initialize() {
        log.info("Performing controller initialisation.");
        
        // TODO actual initialisation
        
        log.info("Initialisation complete.");
    }
}
