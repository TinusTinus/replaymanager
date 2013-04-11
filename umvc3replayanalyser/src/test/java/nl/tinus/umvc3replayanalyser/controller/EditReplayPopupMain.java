package nl.tinus.umvc3replayanalyser.controller;

import java.io.IOException;

import nl.tinus.umvc3replayanalyser.gui.Popups;
import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Side;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class which lets us test the edit replay popup as a standalone application.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class EditReplayPopupMain extends Application {
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

        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, null, Umvc3Character.WOLVERINE, null,
                Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus, Side.PLAYER_ONE);
        
        Popups.showEditReplayPopup(new EditReplayController(game));

        log.info("Application started.");
    }
    
    /** {@inheritDoc} */
    @Override
    public void stop() {
        log.info("Stopping application.");
    }
}
