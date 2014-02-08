// Generated by delombok at Sat Feb 08 23:15:03 CET 2014
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A game of Ultimate Marvel vs Capcom 3.
 *
 * @author Martijn van de Rijdt
 */
public class Game {

    /**
     * Regular expression for which characters are allowed in a player name in a filename. Any other characters will be
     * filtered out and replaced by underscores.
     */
    private static final String WHITELIST_CHARACTERS = "\\W+";

    /**
     * Thread-local variable holding the time format for output filenames. This variable is stored as a thread-local
     * instead of just a single constant, because SimpleDateFormat is not threadsafe.
     */
    private static final ThreadLocal<DateFormat> FILENAME_TIME_FORMAT = new ThreadLocal<DateFormat>() {
        /** {@inheritDoc} */
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * First player.
     */
    @NonNull
    private final Player playerOne;

    /**
     * Player one's team.
     */
    @NonNull
    private final Team teamOne;

    /**
     * Second player.
     */
    @NonNull
    private final Player playerTwo;

    /**
     * Player two's team.
     */
    @NonNull
    private final Team teamTwo;

    /**
     * Convenience constructor, for when the winning player is unknown.
     *
     * @param playerOne
     *            first player
     * @param teamOne
     *            player one's team
     * @param playerTwo
     *            second player
     * @param teamTwo
     *            player two's team
     */
    @JsonCreator
    public Game(@JsonProperty("playerOne") @NonNull Player playerOne, @JsonProperty("teamOne") @NonNull Team teamOne,
            @JsonProperty("playerTwo") @NonNull Player playerTwo, @JsonProperty("teamTwo") @NonNull Team teamTwo) {
        if (playerOne == null) {
            throw new java.lang.NullPointerException("playerOne");
        }
        if (teamOne == null) {
            throw new java.lang.NullPointerException("teamOne");
        }
        if (playerTwo == null) {
            throw new java.lang.NullPointerException("playerTwo");
        }
        if (teamTwo == null) {
            throw new java.lang.NullPointerException("teamTwo");
        }
        this.playerOne = playerOne;
        this.teamOne = teamOne;
        this.playerTwo = playerTwo;
        this.teamTwo = teamTwo;
    }

    /**
     * Returns the player playing on the given side.
     * 
     * @param side
     *            side
     * @return player
     */
    public Player getPlayer(Side side) {
        Player result;
        if (side == Side.PLAYER_ONE) {
            result = playerOne;
        } else if (side == Side.PLAYER_TWO) {
            result = playerTwo;
        } else {
            throw new IllegalArgumentException("Unexpected side: " + side);
        }
        return result;
    }

    /**
     * Returns the team played on the given side.
     * 
     * @param side
     *            side
     * @return team
     */
    public Team getTeam(Side side) {
        Team result;
        if (side == Side.PLAYER_ONE) {
            result = teamOne;
        } else if (side == Side.PLAYER_TWO) {
            result = teamTwo;
        } else {
            throw new IllegalArgumentException("Unexpected side: " + side);
        }
        return result;
    }

    /**
     * Returns an unmodifiable list containing all players.
     * 
     * @return players
     */
    @JsonIgnore
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(Arrays.asList(playerOne, playerTwo));
    }

    /**
     * Returns a description of the match.
     * 
     * @param includeAssists
     *            whether assist names should be included in the description
     * @return match description
     */
    public String getDescription(boolean includeAssists) {
        StringBuffer text = new StringBuffer();
        text.append(playerOne.getGamertag());
        text.append(" (");
        if (includeAssists) {
            text.append(teamOne.getNameWithAssistMoveNames());
        } else {
            text.append(teamOne.getName());
        }
        text.append(") vs. ");
        text.append(playerTwo.getGamertag());
        text.append(" (");
        if (includeAssists) {
            text.append(teamTwo.getNameWithAssistMoveNames());
        } else {
            text.append(teamTwo.getName());
        }
        text.append(")");
        return text.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getDescription(true);
    }

    /**
     * Constructs a base filename, without the extension, to be used for files that have something to do with this game
     * (such as preview image, video file and replay file).
     * 
     * @param date
     *            timestamp for use as the first part of the filename; should indicate when the game was played
     * @return base filename
     */
    public String getBaseFilename(Date date) {
        String time = FILENAME_TIME_FORMAT.get().format(date);
        String playerOneGamertag = this.getPlayerOne().getGamertag().replaceAll(WHITELIST_CHARACTERS, "_");
        String teamOneCharacterOne = this.getTeamOne().getFirstCharacter().getShortName();
        String teamOneCharacterTwo = this.getTeamOne().getSecondCharacter().getShortName();
        String teamOneCharacterThree = this.getTeamOne().getThirdCharacter().getShortName();
        String playerTwoGamertag = this.getPlayerTwo().getGamertag().replaceAll(WHITELIST_CHARACTERS, "_");
        String teamTwoCharacterOne = this.getTeamTwo().getFirstCharacter().getShortName();
        String teamTwoCharacterTwo = this.getTeamTwo().getSecondCharacter().getShortName();
        String teamTwoCharacterThree = this.getTeamTwo().getThirdCharacter().getShortName();
        String result = String.format("%s-%s(%s-%s-%s)_vs_%s(%s-%s-%s)", time, playerOneGamertag, teamOneCharacterOne,
                teamOneCharacterTwo, teamOneCharacterThree, playerTwoGamertag, teamTwoCharacterOne,
                teamTwoCharacterTwo, teamTwoCharacterThree);
        return result;
    }

    /**
     * First player.
     */
    @NonNull
    @java.lang.SuppressWarnings("all")
    public Player getPlayerOne() {
        return this.playerOne;
    }

    /**
     * Player one's team.
     */
    @NonNull
    @java.lang.SuppressWarnings("all")
    public Team getTeamOne() {
        return this.teamOne;
    }

    /**
     * Second player.
     */
    @NonNull
    @java.lang.SuppressWarnings("all")
    public Player getPlayerTwo() {
        return this.playerTwo;
    }

    /**
     * Player two's team.
     */
    @NonNull
    @java.lang.SuppressWarnings("all")
    public Team getTeamTwo() {
        return this.teamTwo;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Game))
            return false;
        final Game other = (Game) o;
        if (!other.canEqual((java.lang.Object) this))
            return false;
        final java.lang.Object this$playerOne = this.getPlayerOne();
        final java.lang.Object other$playerOne = other.getPlayerOne();
        if (this$playerOne == null ? other$playerOne != null : !this$playerOne.equals(other$playerOne))
            return false;
        final java.lang.Object this$teamOne = this.getTeamOne();
        final java.lang.Object other$teamOne = other.getTeamOne();
        if (this$teamOne == null ? other$teamOne != null : !this$teamOne.equals(other$teamOne))
            return false;
        final java.lang.Object this$playerTwo = this.getPlayerTwo();
        final java.lang.Object other$playerTwo = other.getPlayerTwo();
        if (this$playerTwo == null ? other$playerTwo != null : !this$playerTwo.equals(other$playerTwo))
            return false;
        final java.lang.Object this$teamTwo = this.getTeamTwo();
        final java.lang.Object other$teamTwo = other.getTeamTwo();
        if (this$teamTwo == null ? other$teamTwo != null : !this$teamTwo.equals(other$teamTwo))
            return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    public boolean canEqual(final java.lang.Object other) {
        return other instanceof Game;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 277;
        int result = 1;
        final java.lang.Object $playerOne = this.getPlayerOne();
        result = result * PRIME + ($playerOne == null ? 0 : $playerOne.hashCode());
        final java.lang.Object $teamOne = this.getTeamOne();
        result = result * PRIME + ($teamOne == null ? 0 : $teamOne.hashCode());
        final java.lang.Object $playerTwo = this.getPlayerTwo();
        result = result * PRIME + ($playerTwo == null ? 0 : $playerTwo.hashCode());
        final java.lang.Object $teamTwo = this.getTeamTwo();
        result = result * PRIME + ($teamTwo == null ? 0 : $teamTwo.hashCode());
        return result;
    }
}