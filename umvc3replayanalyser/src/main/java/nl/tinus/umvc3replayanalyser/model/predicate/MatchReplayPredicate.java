package nl.tinus.umvc3replayanalyser.model.predicate;

import lombok.ToString;
import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Replay;
import nl.tinus.umvc3replayanalyser.model.Side;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import com.google.common.base.Predicate;

/**
 * Predicate for matching a replay.
 * 
 * @author Martijn van de Rijdt
 */
@ToString
public class MatchReplayPredicate implements Predicate<Replay> {
    /** Player predicate. */
    private final Predicate<Player> playerPredicate;
    /** Team predicate. */
    private final Predicate<Team> teamPredicate;
    /** Side to be matched. */
    private final Side side;

    /**
     * Constructor.
     * 
     * @param prefix
     *            prefix to be matched to the player's gamertag; may not be null
     * @param character1
     *            character 1
     * @param character2
     *            character 2
     * @param character3
     *            character 3
     * @param assist1
     *            assist type for character 1
     * @param assist2
     *            assist type for character 2
     * @param assist3
     *            assist type for character 3
     * @param maintainCharacterOrder
     *            whether character order needs to be respected when matching against a team
     * @param side
     *            side; may not be null
     */
    public MatchReplayPredicate(String prefix, Umvc3Character character1, Umvc3Character character2,
            Umvc3Character character3, AssistType assist1, AssistType assist2, AssistType assist3,
            boolean maintainCharacterOrder, Side side) {
        super();
        
        if (side == null) {
            throw new NullPointerException("side");
        }
        
        this.playerPredicate = new GamertagPrefixPlayerPredicate(prefix);
        this.teamPredicate = new MatchTeamPredicate(character1, character2, character3, assist1, assist2, assist3,
                maintainCharacterOrder);
        this.side = side;
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(Replay replay) {
        return playerPredicate.apply(replay.getGame().getPlayer(side))
                && teamPredicate.apply(replay.getGame().getTeam(side));
    }
}
