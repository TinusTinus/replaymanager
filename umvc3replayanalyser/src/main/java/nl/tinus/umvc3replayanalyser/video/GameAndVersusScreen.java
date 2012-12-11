package nl.tinus.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.tinus.umvc3replayanalyser.model.Game;

/**
 * Container for a game and the corresponding versus screen.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@ToString
@EqualsAndHashCode
public class GameAndVersusScreen {
    /** Game. */
    private final Game game;
    /** Versus screen. */
    private final BufferedImage versusScreen;
}
