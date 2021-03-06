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
package nl.mvdr.umvc3replayanalyser.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

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
    /** Location of the replay video, relative to the data directory. */
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
    public Replay(@JsonProperty("creationTime") @NonNull Date creationTime, @JsonProperty("game") @NonNull Game game,
            @JsonProperty("videoLocation") @NonNull String videoLocation,
            @JsonProperty("previewImageLocation") @NonNull String previewImageLocation) {
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
