package nl.tinus.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Side;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.ocr.OCREngine;
import nl.tinus.umvc3replayanalyser.ocr.OCRException;

/**
 * Analyses an image of an Ultimate Marvel vs Capcom 3 versus screen, using optical character recognition (OCR) to
 * determine player and character names.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor
@Slf4j
public class VersusScreenAnalyser {
    /** Width of the vs screen. */
    public static final int SCREEN_WIDTH = 1280;
    /** Height of the vs screen. */
    public static final int SCREEN_HEIGHT = 720;
    /** Width of a gamertag. */
    private static final int PLAYER_WIDTH = 261;
    /** Height of a gamertag. */
    private static final int PLAYER_HEIGHT = 29;
    /** X coordinate of the upper left corner of the rectangle containing player one's gamertag. */
    private static final int PLAYER_ONE_X = 164;
    /** X coordinate of the upper left corner of the rectangle containing player two's gamertag. */
    private static final int PLAYER_TWO_X = 910;
    /** Y coordinate of the upper left corner of the rectangle containing either player's gamertag. */
    private static final int PLAYER_Y = 425;
    /** X coordinate of the upper left corner of player one's first character. */
    private static final int CHARACTER_1_PLAYER_ONE_X = 291;
    /** X coordinate of the upper left corner of player two's first character. */
    private static final int CHARACTER_1_PLAYER_TWO_X = 833;
    /** X coordinate of the upper left corner of player one's second character. */
    private static final int CHARACTER_2_PLAYER_ONE_X = 175;
    /** X coordinate of the upper left corner of player two's second character. */
    private static final int CHARACTER_2_PLAYER_TWO_X = 714;
    /** X coordinate of the upper left corner of player one's third character. */
    private static final int CHARACTER_3_PLAYER_ONE_X = 403;
    /** X coordinate of the upper left corner of player two's third character. */
    private static final int CHARACTER_3_PLAYER_TWO_X = 938;
    /** Y coordinate of the upper left corner of either player's first character. */
    private static final int CHARACTER_1_Y = 579;
    /** Y coordinate of the upper left corner of either player's second character. */
    private static final int CHARACTER_2_Y = 622;
    /** Y coordinate of the upper left corner of either player's third character. */
    private static final int CHARACTER_3_Y = 615;
    /** Width of a character name. */
    private static final int CHARACTER_WIDTH = 192;
    /** Height of a character name. */
    private static final int CHARACTER_HEIGHT = 43;

    /** OCR Engine. */
    private final OCREngine engine;

    /**
     * Analyses the given image and returns the Game represented by this image.
     * 
     * @param versusImage
     *            image to be analysed, should be a 1280 x 720 versus screen
     * @return game represented by this image; all fields are filled except winningSide and assists
     * @throws OCRException
     *             in case image analysis fails
     */
    // TODO drop the image size requirement by having all sizes be a percentage of the total image size.
    // Don't forget to fix Javadoc as well and reduce visibility of SCREEN_WIDTH and SCREEN_HEIGHT.
    public Game analyse(BufferedImage versusImage) throws OCRException {
        // Check the image size.
        if (versusImage.getWidth() != SCREEN_WIDTH || versusImage.getHeight() != SCREEN_HEIGHT) {
            throw new IllegalArgumentException(String.format("Image size must be %s x %s, was %s x %s", ""
                    + SCREEN_WIDTH, "" + SCREEN_HEIGHT, "" + versusImage.getWidth(), "" + versusImage.getHeight()));
        }

        String playerOneGamertag = getGamerTag(versusImage, Side.PLAYER_ONE, PLAYER_ONE_X, PLAYER_Y, PLAYER_WIDTH,
                PLAYER_HEIGHT);
        String playerTwoGamertag = getGamerTag(versusImage, Side.PLAYER_TWO, PLAYER_TWO_X, PLAYER_Y, PLAYER_WIDTH,
                PLAYER_HEIGHT);

        return analyse(versusImage, playerOneGamertag, playerTwoGamertag);
    }

    /**
     * Analyses the given image and returns the Game represented by this image. Doesn't attempt to read the gamertags
     * from the image. Useful for offline matches (where the gamertags aren't displayed on the versus screen).
     * 
     * @param playerOneGamertag
     *            gamertag of player one
     * @param playerTwoGamertag
     *            gamertag of player two
     * @param versusImage
     *            image to be analysed, should be a 1280 x 720 versus screen
     * @return game represented by this image; all fields are filled except winningSide and assists
     * @throws OCRException
     *             in case optical character recognition fails
     */
    public Game analyse(BufferedImage versusImage, String playerOneGamertag, String playerTwoGamertag)
            throws OCRException {
        // Check the image size.
        if (versusImage.getWidth() != SCREEN_WIDTH || versusImage.getHeight() != SCREEN_HEIGHT) {
            throw new IllegalArgumentException(String.format("Image size must be %s x %s, was %s x %s", ""
                    + SCREEN_WIDTH, "" + SCREEN_HEIGHT, "" + versusImage.getWidth(), "" + versusImage.getHeight()));
        }

        Umvc3Character firstCharacterTeamOne = getCharacter(versusImage, Side.PLAYER_ONE, 1, CHARACTER_1_PLAYER_ONE_X,
                CHARACTER_1_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        Umvc3Character secondCharacterTeamOne = getCharacter(versusImage, Side.PLAYER_ONE, 2, CHARACTER_2_PLAYER_ONE_X,
                CHARACTER_2_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        Umvc3Character thirdCharacterTeamOne = getCharacter(versusImage, Side.PLAYER_ONE, 3, CHARACTER_3_PLAYER_ONE_X,
                CHARACTER_3_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT);

        Umvc3Character firstCharacterTeamTwo = getCharacter(versusImage, Side.PLAYER_TWO, 1, CHARACTER_1_PLAYER_TWO_X,
                CHARACTER_1_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        Umvc3Character secondCharacterTeamTwo = getCharacter(versusImage, Side.PLAYER_TWO, 2, CHARACTER_2_PLAYER_TWO_X,
                CHARACTER_2_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        Umvc3Character thirdCharacterTeamTwo = getCharacter(versusImage, Side.PLAYER_TWO, 3, CHARACTER_3_PLAYER_TWO_X,
                CHARACTER_3_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT);

        Player playerOne = new Player(playerOneGamertag);
        Player playerTwo = new Player(playerTwoGamertag);

        Team teamOne = new Team(firstCharacterTeamOne, secondCharacterTeamOne, thirdCharacterTeamOne);
        Team teamTwo = new Team(firstCharacterTeamTwo, secondCharacterTeamTwo, thirdCharacterTeamTwo);

        Game game = new Game(playerOne, teamOne, playerTwo, teamTwo);

        log.info("Read game: " + game);

        return game;
    }

    /**
     * Reads a player's gamertag from the given image.
     * 
     * @param versusImage
     *            image
     * @param side
     *            side
     * @param x
     *            x-coordinate of the upper left corner of the rectangle where the gamertag is located
     * @param y
     *            y-coordinate of the upper left corner of the rectangle where the gamertag is located
     * @param w
     *            width of the gamertag
     * @param h
     *            height of the gamertag
     * @return gamertag
     * @throws OCRException
     *             in case OCR fails
     */
    private String getGamerTag(BufferedImage versusImage, Side side, int x, int y, int w, int h) throws OCRException {
        BufferedImage image = versusImage.getSubimage(x, y, w, h);
        String result = this.engine.ocrLine(image);
        log.info(side + "'s gamertag: " + result);
        return result;
    }

    /**
     * Reads a character from the given image.
     * 
     * @param versusImage
     *            image
     * @param side
     *            side
     * @param characterNumber
     *            should be 1, 2 or 3; purely for logging
     * @param x
     *            x-coordinate of the upper left corner of the rectangle where the gamertag is located
     * @param y
     *            y-coordinate of the upper left corner of the rectangle where the gamertag is located
     * @param w
     *            width of the gamertag
     * @param h
     *            height of the gamertag
     * @return gamertag
     * @throws OCRException
     *             in case OCR fails
     */
    private Umvc3Character getCharacter(BufferedImage versusImage, Side side, int characterNumber, int x, int y, int w,
            int h) throws OCRException {
        BufferedImage image = versusImage.getSubimage(x, y, w, h);
        Umvc3Character result = this.engine.ocrCharacter(image);
        log.info(String.format("%s's character %s: %s", side, "" + characterNumber, result));

        return result;
    }
}
