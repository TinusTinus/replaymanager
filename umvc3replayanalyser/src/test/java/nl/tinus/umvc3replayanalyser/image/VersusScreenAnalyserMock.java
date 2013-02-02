package nl.tinus.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.ocr.OCRException;

/**
 * Mock implementation of the VersusScreenAnalyser interface, for use in unit tests.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class VersusScreenAnalyserMock implements VersusScreenAnalyser {
    /** The dummy game returned by this mock's analyse method. */
    public static final Game DUMMY_GAME = new Game(new Player("Player One"), new Team(Umvc3Character.VERGIL,
            Umvc3Character.DANTE, Umvc3Character.TRISH), new Player("Player Two"), new Team(Umvc3Character.RYU,
            Umvc3Character.CHUN_LI, Umvc3Character.AKUMA));

    /** The number of times the analyse method has been called. */
    @Getter
    private int numberOfCalls;

    /** Constructor. */
    public VersusScreenAnalyserMock() {
        super();
        this.numberOfCalls = 0;
    }

    /** {@inheritDoc} */
    @Override
    public Game analyse(BufferedImage versusImage) throws OCRException {
        log.info("analyse called: " + versusImage);
        this.numberOfCalls++;
        return DUMMY_GAME;
    }
}
