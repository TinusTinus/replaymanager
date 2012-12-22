package nl.tinus.umvc3replayanalyser.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Representation of an Ultimate Marvel vs Capcom 3 replay.
 * 
 * @author Martijn van de Rijdt
 */
@Getter
@EqualsAndHashCode
@ToString
public class Replay {
    /**
     * Thread-local variable holding the date format. This variable is stored as a thread-local instead of just a single
     * constant, because SimpleDateFormat is not threadsafe.
     */
    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
        /** {@inheritDoc} */
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /** Moment the replay was created (meaning when the game was played). */
    @NonNull
    private final Date creationTime;
    /** Data about the game. */
    @NonNull
    private final Game game;
    /** Location of the replay video. */
    @NonNull
    private String videoLocation;
    /** Location of the replay's preview image. */
    @NonNull
    private String previewImageLocation;

    /**
     * Constructor.
     * 
     * @param creationTime
     *            moment the replay was created (meaning when the game was played)
     * @param game
     *            data about the game
     * @param videoLocation
     *            location of the replay video
     * @param previewImageLocation
     *            location of the replay's preview image
     */
    @JsonCreator
    public Replay(@JsonProperty("creationTime") Date creationTime, @JsonProperty("game") Game game,
            @JsonProperty("videoLocation") String videoLocation,
            @JsonProperty("previewImageLocation") String previewImageLocation) {
        super();
        this.creationTime = creationTime;
        this.game = game;
        this.videoLocation = videoLocation;
        this.previewImageLocation = previewImageLocation;
    }

    /**
     * Returns a string representation of the game's creation time.
     * 
     * @return creation time
     */
    @JsonIgnore
    public String getCreationTimeString() {
        return DATE_FORMAT.get().format(creationTime);
    }

    /**
     * Delegate getter for player one.
     * 
     * @return player one
     */
    @JsonIgnore
    public Player getPlayerOne() {
        return this.game.getPlayerOne();
    }

    /**
     * Delegate getter for player two.
     * 
     * @return player two
     */
    @JsonIgnore
    public Player getPlayerTwo() {
        return this.game.getPlayerTwo();
    }

    /**
     * Delegate getter for the first character on player one's team.
     * 
     * @return character
     */
    @JsonIgnore
    public Umvc3Character getTeamOneFirstCharacter() {
        return this.game.getTeamOne().getFirstCharacter();
    }

    /**
     * Delegate getter for the second character on player one's team.
     * 
     * @return character
     */
    @JsonIgnore
    public Umvc3Character getTeamOneSecondCharacter() {
        return this.game.getTeamOne().getSecondCharacter();
    }

    /**
     * Delegate getter for the third character on player one's team.
     * 
     * @return character
     */
    @JsonIgnore
    public Umvc3Character getTeamOneThirdCharacter() {
        return this.game.getTeamOne().getThirdCharacter();
    }

    /**
     * Delegate getter for the first character on player two's team.
     * 
     * @return character
     */
    @JsonIgnore
    public Umvc3Character getTeamTwoFirstCharacter() {
        return this.game.getTeamTwo().getFirstCharacter();
    }

    /**
     * Delegate getter for the second character on player two's team.
     * 
     * @return character
     */
    @JsonIgnore
    public Umvc3Character getTeamTwoSecondCharacter() {
        return this.game.getTeamTwo().getSecondCharacter();
    }

    /**
     * Delegate getter for the third character on player two's team.
     * 
     * @return character
     */
    @JsonIgnore
    public Umvc3Character getTeamTwoThirdCharacter() {
        return this.game.getTeamTwo().getThirdCharacter();
    }
}
