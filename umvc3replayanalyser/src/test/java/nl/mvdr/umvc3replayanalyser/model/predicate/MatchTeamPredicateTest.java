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

import nl.mvdr.umvc3replayanalyser.model.AssistType;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MatchTeamPredicate}.
 * 
 * @author Martijn van de Rijdt
 */
public class MatchTeamPredicateTest {
    /** Tests the apply method. */
    @Test
    public void testMatchAny() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(null, null, null, null, null, null, false);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchAnyMaintainOrder() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(null, null, null, null, null, null, true);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchCharacters() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(Umvc3Character.WOLVERINE, null, Umvc3Character.ZERO,
                null, Umvc3Character.DOCTOR_DOOM, null, false);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchCharactersMaintainOrder() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(Umvc3Character.WOLVERINE, null, Umvc3Character.ZERO,
                null, Umvc3Character.DOCTOR_DOOM, null, true);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchCharactersShuffled() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(Umvc3Character.ZERO, null, Umvc3Character.WOLVERINE,
                null, Umvc3Character.DOCTOR_DOOM, null, false);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchCharactersShuffledMaintainOrder() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(Umvc3Character.ZERO, null, Umvc3Character.WOLVERINE,
                null, Umvc3Character.DOCTOR_DOOM, null, true);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertFalse(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchOneCharacter() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(Umvc3Character.WOLVERINE, null, null, null, null, null,
                false);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchOneCharacterMaintainOrder() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(Umvc3Character.WOLVERINE, null, null, null, null, null,
                true);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchOneCharacterShuffled() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(null, null, Umvc3Character.WOLVERINE, null, null, null,
                false);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertTrue(predicate.apply(team));
    }

    /** Tests the apply method. */
    @Test
    public void testMatchOneCharacterShuffledMaintainOrder() {
        MatchTeamPredicate predicate = new MatchTeamPredicate(null, null, Umvc3Character.WOLVERINE, null, null, null,
                true);
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.ALPHA, Umvc3Character.ZERO, AssistType.GAMMA,
                Umvc3Character.DOCTOR_DOOM, AssistType.GAMMA);

        Assert.assertFalse(predicate.apply(team));
    }
}
