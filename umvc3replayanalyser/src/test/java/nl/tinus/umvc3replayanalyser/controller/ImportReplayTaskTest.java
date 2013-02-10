package nl.tinus.umvc3replayanalyser.controller;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nl.tinus.umvc3replayanalyser.config.ConfigurationDummy;
import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.video.ReplayAnalyserDummy;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for ImportReplayTask.
 * 
 * @author Martijn van de Rijdt
 */
public class ImportReplayTaskTest {
    /** Tests the construction of the base filename. */
    @Test
    public void testGetBaseFilename() {
        // Create task to be tested.
        File dataDirectory = new File("src/test/resources/data");
        List<Replay> replays = Collections.emptyList();
        ConfigurationDummy configuration = new ConfigurationDummy() {
            /** {@inheritDoc} */
            @Override
            public boolean isPrettyPrintReplays() {
                return false;
            }
        };
        ImportReplayTask task = new ImportReplayTask(dataDirectory, replays, configuration, new ReplayAnalyserDummy());
        
        Date creationTime = new GregorianCalendar(2012, 0, 4, 18, 25, 46).getTime();
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus);
        
        String baseFilename = task.getBaseFilename(game, creationTime);
        
        Assert.assertEquals("20120104182546-DJ_Alberto_Lara(Hulk-Wolvie-Sent)_vs_MvdR(Wolvie-Zero-Doom)", baseFilename);
    }
    
    /** Tests the construction of the base filename where the player names contain invalid symbols. */
    @Test
    public void testGetBaseFilenameSymbols() {
        // Create task to be tested.
        File dataDirectory = new File("src/test/resources/data");
        List<Replay> replays = Collections.emptyList();
        ConfigurationDummy configuration = new ConfigurationDummy() {
            /** {@inheritDoc} */
            @Override
            public boolean isPrettyPrintReplays() {
                return false;
            }
        };
        ImportReplayTask task = new ImportReplayTask(dataDirectory, replays, configuration, new ReplayAnalyserDummy());
        
        Date creationTime = new GregorianCalendar(2012, 0, 4, 18, 25, 46).getTime();
        Player playerOne = new Player("Pipe|Pipe");
        Team teamOne = new Team(Umvc3Character.RYU, Umvc3Character.AKUMA, Umvc3Character.C_VIPER);
        Player playerTwo = new Player("Star*Star");
        Team teamTwo = new Team(Umvc3Character.CAPTAIN_AMERICA, Umvc3Character.IRON_MAN, Umvc3Character.THOR);
        Game game = new Game(playerOne, teamOne, playerTwo, teamTwo);
        
        String baseFilename = task.getBaseFilename(game, creationTime);
        
        Assert.assertEquals("20120104182546-Pipe_Pipe(Ryu-Akuma-Viper)_vs_Star_Star(Cap-IronMan-Thor)", baseFilename);
    }
}
