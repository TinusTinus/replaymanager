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
package nl.tinus.umvc3replayanalyser.model.predicate;

import java.util.Arrays;
import java.util.List;

import lombok.ToString;
import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import com.google.common.base.Predicate;

/**
 * Predicate for matching a team.
 * 
 * @author Martijn van de Rijdt
 */
@ToString
public class MatchTeamPredicate implements Predicate<Team> {
    /** Characters. */
    private final List<Umvc3Character> characters;
    /** Assist types. */
    private final List<AssistType> assists;
    /** Whether character order needs to be respected when matching against a team. */
    private final boolean maintainCharacterOrder;

    /**
     * Constructor.
     * 
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
     * @param meaintainCharacterOrder
     *            whether character order needs to be respected when matching against a team
     */
    public MatchTeamPredicate(Umvc3Character character1, AssistType assist1, Umvc3Character character2,
            AssistType assist2, Umvc3Character character3, AssistType assist3, boolean maintainCharacterOrder) {
        super();
        this.characters = Arrays.asList(character1, character2, character3);
        this.assists = Arrays.asList(assist1, assist2, assist3);
        this.maintainCharacterOrder = maintainCharacterOrder;
    }

    /** {@inheritDoc} */
    @Override
    public boolean apply(Team team) {
        boolean result = true;
        int i = 0;
        while (result && i != 3) {
            Umvc3Character character = characters.get(i);
            if (character != null) {
                AssistType assist = assists.get(i);
                if (maintainCharacterOrder) {
                    result = character == team.getCharacters().get(i);
                    result = result && (assist == null || assist == team.getAssists().get(i));
                } else {
                    int characterIndex = team.getCharacters().indexOf(character);
                    result = 0 <= characterIndex;
                    result = result && (assist == null || assist == team.getAssists().get(characterIndex));
                }
            }
            i++;
        }
        return result;
    }
}
