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
package nl.mvdr.umvc3replayanalyser.model.predicate;

import lombok.NonNull;
import lombok.ToString;
import nl.mvdr.umvc3replayanalyser.model.AssistType;
import nl.mvdr.umvc3replayanalyser.model.Player;
import nl.mvdr.umvc3replayanalyser.model.Replay;
import nl.mvdr.umvc3replayanalyser.model.Side;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

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
    @NonNull
    private final Side side;

    /**
     * Constructor.
     * 
     * @param prefix
     *            prefix to be matched to the player's gamertag; may not be null
     * @param character1
     *            character 1
     * @param assist1
     *            assist type for character 1
     * @param character2
     *            character 2
     * @param assist2
     *            assist type for character 2
     * @param character3
     *            character 3
     * @param assist3
     *            assist type for character 3
     * @param maintainCharacterOrder
     *            whether character order needs to be respected when matching against a team
     * @param side
     *            side; may not be null
     */
    public MatchReplayPredicate(String prefix, Umvc3Character character1, AssistType assist1,
            Umvc3Character character2, AssistType assist2, Umvc3Character character3, AssistType assist3,
            boolean maintainCharacterOrder, Side side) {
        super();

        if (side == null) {
            throw new NullPointerException("side");
        }

        if ("".equals(prefix)) {
            this.playerPredicate = Predicates.alwaysTrue();
        } else {
            this.playerPredicate = new GamertagPrefixPlayerPredicate(prefix);
        }

        if (character1 == null && character2 == null && character3 == null) {
            this.teamPredicate = Predicates.alwaysTrue();
        } else {
            this.teamPredicate = new MatchTeamPredicate(character1, assist1, character2, assist2, character3, assist3,
                    maintainCharacterOrder);
        }
        this.side = side;
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(Replay replay) {
        return playerPredicate.apply(replay.getGame().getPlayer(side))
                && teamPredicate.apply(replay.getGame().getTeam(side));
    }
}
