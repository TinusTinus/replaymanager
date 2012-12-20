package nl.tinus.umvc3replayanalyser.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * A game of Ultimate Marvel vs Capcom 3.
 * 
 * @author Martijn van de Rijdt
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Game {
    /** First player. */
    @NonNull
    private final Player playerOne;
    /** Player one's team. */
    @NonNull
    private final Team teamOne;
    /** Second player. */
    @NonNull
    private final Player playerTwo;
    /** Player two's team. */
    @NonNull
    private final Team teamTwo;
    /** Winning side. May be null in case the winner is unknown, or it is a draw game. */
    private final Side winningSide;

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
    public Game(@JsonProperty("playerOne") Player playerOne, @JsonProperty("teamOne") Team teamOne,
            @JsonProperty("playerTwo") Player playerTwo, @JsonProperty("teamTwo") Team teamTwo) {
        this(playerOne, teamOne, playerTwo, teamTwo, null);
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
     * @return characters
     */
    @JsonIgnore
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(Arrays.asList(playerOne, playerTwo));
    }

    /** @return the losing side, or null in case of a draw game */
    @JsonIgnore
    public Side getLosingSide() {
        Side result;
        if (winningSide != null) {
            result = winningSide.getOpposite();
        } else {
            result = null;
        }
        return result;
    }

    /** @return the winning player, or null in case of a draw game */
    @JsonIgnore
    public Player getWinningPlayer() {
        Player result;
        if (winningSide != null) {
            result = getPlayer(winningSide);
        } else {
            result = null;
        }
        return result;
    }

    /** @return the winning player, or null in case of a draw game */
    @JsonIgnore
    public Player getLosingPlayer() {
        Player result;
        Side losingSide = getLosingSide();
        if (losingSide != null) {
            result = getPlayer(losingSide);
        } else {
            result = null;
        }
        return result;
    }

    /**
     * Returns a description of the match.
     * 
     * @param includeAssists
     *            whether assist names should be included in the description
     * @param includeWinner
     *            if true, the description will indicate which player won by putting "(W)" after their gamertag
     * @return match description
     */
    public String getDescription(boolean includeAssists, boolean includeWinner) {
        StringBuffer text = new StringBuffer();

        text.append(playerOne.getGamertag());
        text.append(" (");
        if (includeAssists) {
            text.append(teamOne.getNameWithAssistMoveNames());
        } else {
            text.append(teamOne.getName());
        }
        text.append(")");
        if (includeWinner && winningSide == Side.PLAYER_ONE) {
            text.append(" (W)");
        }

        text.append(" vs. ");

        text.append(playerTwo.getGamertag());
        text.append(" (");
        if (includeAssists) {
            text.append(teamTwo.getNameWithAssistMoveNames());
        } else {
            text.append(teamTwo.getName());
        }
        text.append(")");
        if (includeWinner && winningSide == Side.PLAYER_TWO) {
            text.append(" (W)");
        }

        return text.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getDescription(true, true);
    }
}
