package nl.tinus.umvc3replayanalyser.model.predicate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tinus.umvc3replayanalyser.model.Player;

import com.google.common.base.Predicate;

/**
 * Predicate that checks whether the player's gamertag starts with the given string.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor
public class GamertagPrefixPlayerPredicate implements Predicate<Player> {
    /** Name prefix to be matched. */
    @NonNull
    private final String prefix;
    
    /** {@inheritDoc} */
    @Override
    public boolean apply(Player player) {
        return player.getGamertag().startsWith(prefix);
    }
}
