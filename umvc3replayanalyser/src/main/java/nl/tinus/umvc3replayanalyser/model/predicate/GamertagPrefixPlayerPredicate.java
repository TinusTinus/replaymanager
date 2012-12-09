package nl.tinus.umvc3replayanalyser.model.predicate;

import org.apache.commons.lang3.StringUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.tinus.umvc3replayanalyser.model.Player;

import com.google.common.base.Predicate;

/**
 * Predicate that checks whether the player's gamertag starts with the given string.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor
@ToString
public class GamertagPrefixPlayerPredicate implements Predicate<Player> {
    /** Name prefix to be matched. */
    @NonNull
    private final String prefix;
    
    /** {@inheritDoc} */
    @Override
    public boolean apply(Player player) {
        return StringUtils.startsWithIgnoreCase(player.getGamertag(), prefix);
    }
}
