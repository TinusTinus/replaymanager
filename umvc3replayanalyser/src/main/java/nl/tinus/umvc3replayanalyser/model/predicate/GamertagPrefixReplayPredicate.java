package nl.tinus.umvc3replayanalyser.model.predicate;

import lombok.ToString;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.model.Side;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Predicate that checks whether one of the player's gamertags starts with the given string.
 * 
 * @author Martijn van de Rijdt
 */
@ToString
public class GamertagPrefixReplayPredicate implements Predicate<Replay> {
    /** Predicate for checking a player's gamertag. */
    private final GamertagPrefixPlayerPredicate playerPredicate;
    /** Side to match. If null both sides will be matched. */
    private final Side side;
    
    /**
     * Constructor.
     * 
     * @param prefix prefix to be matched to the player's gamertag; may not be null
     * @param side side to match; if null both sides will be matched
     */
    public GamertagPrefixReplayPredicate(String prefix, Side side) {
        super();
        this.playerPredicate = new GamertagPrefixPlayerPredicate(prefix);
        this.side = side;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean apply(Replay replay) {
        boolean result;
        if (side != null) {
            Player player = replay.getGame().getPlayer(side);
            result = playerPredicate.apply(player);
        } else {
            result = Iterables.any(replay.getGame().getPlayers(), playerPredicate);
        }
        return result;
    }
}
