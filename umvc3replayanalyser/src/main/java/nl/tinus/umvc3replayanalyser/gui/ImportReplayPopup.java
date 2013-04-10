package nl.tinus.umvc3replayanalyser.gui;

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Class used to load and show the import replay view.
 * 
 * This is a utility class that cannot be instantiated; use the static methods.
 * 
 * @author Martijn van de Rijdt
 * 
 * @deprecated use Popups instead
 */
// private constructor to prevent utility class instantiation
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Deprecated
public class ImportReplayPopup {
    /**
     * Shows the import replay popup in a new stage.
     * 
     * @param controller
     *            controller for this popup window, typically an ImportReplayPopupController
     */
    public static void show(Object controller) {
        show(new Stage(), controller);
    }
    
    /**
     * Shows the import replay popup.
     * 
     * @param stage
     *            stage in which to show the user interface
     * @param controller
     *            controller for this popup window, typically an ImportReplayPopupController
     */
    public static void show(Stage stage, Object controller) {
        Popups.show("/import-replay-popup.fxml", stage, controller, "Importing replays");
    }
}
