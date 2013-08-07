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

import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.model.Assist;
import nl.mvdr.umvc3replayanalyser.model.AssistType;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link Team}.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class TeamTest {
    /** Tests the constructor and the various getName methods. */
    @Test
    public void test() {
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        log.info("" + team);
        Assert.assertEquals("Wolverine / Zero / Doctor Doom", team.getName());
        Assert.assertEquals("Wolverine (gamma) / Zero (alpha) / Doctor Doom (alpha)", team.getNameWithAssists());
        Assert.assertEquals("Wolverine (Berserker Barrage) / Zero (Ryuenjin) / Doctor Doom (Plasma Beam)",
                team.getNameWithAssistMoveNames());
    }

    /** Tests a custom team name. */
    @Test
    public void testCustomTeamNamesTeamTrenchcoat() {
        Team team = new Team(Umvc3Character.VERGIL, AssistType.ALPHA, Umvc3Character.DANTE, AssistType.BETA,
                Umvc3Character.WESKER, AssistType.GAMMA);
        log.info("" + team);
        Assert.assertEquals("Team Trenchcoat", team.getName());
        Assert.assertEquals("Vergil (alpha) / Dante (beta) / Wesker (gamma)", team.getNameWithAssists());
    }

    /** Tests what happens when we try to pass a null character value to the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullValueFirstCharacter() {
        new Team(null, Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM);
    }
    
    /** Tests what happens when we try to pass a null character value to the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullValueSecondCharacter() {
        new Team(Umvc3Character.WOLVERINE, null, Umvc3Character.DOCTOR_DOOM);
    }

    
    /** Tests what happens when we try to pass a null character value to the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullValueThirdCharacter() {
        new Team(Umvc3Character.WOLVERINE, Umvc3Character.ZERO, null);
    }
    
    /** Tests what happens when we try to pass a null assist value to the constructor. */
    @Test
    public void testNullValueFirstAssist() {
        Team team = new Team(Umvc3Character.WOLVERINE, null,
                Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        
        Assert.assertEquals(Umvc3Character.WOLVERINE, team.getFirstCharacter());
        Assert.assertNull(team.getFirstAssistType());
        Assert.assertNull(team.getFirstAssist());
        Assert.assertEquals(Umvc3Character.ZERO, team.getSecondCharacter());
        Assert.assertEquals(AssistType.ALPHA, team.getSecondAssistType());
        Assert.assertEquals(new Assist(AssistType.ALPHA, Umvc3Character.ZERO), team.getSecondAssist());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, team.getThirdCharacter());
        Assert.assertEquals(AssistType.ALPHA, team.getThirdAssistType());
        Assert.assertEquals(new Assist(AssistType.ALPHA, Umvc3Character.DOCTOR_DOOM), team.getThirdAssist());
        
        Assert.assertEquals("Wolverine / Zero (alpha) / Doctor Doom (alpha)", team.getNameWithAssists());
        Assert.assertEquals("Wolverine / Zero (Ryuenjin) / Doctor Doom (Plasma Beam)", team.getNameWithAssistMoveNames());
    }
    
    /** Tests what happens when we try to pass a null assist value to the constructor. */
    @Test
    public void testNullValueSecondAssist() {
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA,
                Umvc3Character.ZERO, null,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        
        Assert.assertEquals(Umvc3Character.WOLVERINE, team.getFirstCharacter());
        Assert.assertEquals(AssistType.GAMMA, team.getFirstAssistType());
        Assert.assertEquals(new Assist(AssistType.GAMMA, Umvc3Character.WOLVERINE), team.getFirstAssist());
        Assert.assertEquals(Umvc3Character.ZERO, team.getSecondCharacter());
        Assert.assertNull(team.getSecondAssistType());
        Assert.assertNull(team.getSecondAssist());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, team.getThirdCharacter());
        Assert.assertEquals(AssistType.ALPHA, team.getThirdAssistType());
        Assert.assertEquals(new Assist(AssistType.ALPHA, Umvc3Character.DOCTOR_DOOM), team.getThirdAssist());
        
        Assert.assertEquals("Wolverine (gamma) / Zero / Doctor Doom (alpha)", team.getNameWithAssists());
        Assert.assertEquals("Wolverine (Berserker Barrage) / Zero / Doctor Doom (Plasma Beam)", team.getNameWithAssistMoveNames());
    }
    
    /** Tests what happens when we try to pass a null assist value to the constructor. */
    @Test
    public void testNullValueThirdAssist() {
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA,
                Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, null);
        
        Assert.assertEquals(Umvc3Character.WOLVERINE, team.getFirstCharacter());
        Assert.assertEquals(AssistType.GAMMA, team.getFirstAssistType());
        Assert.assertEquals(new Assist(AssistType.GAMMA, Umvc3Character.WOLVERINE), team.getFirstAssist());
        Assert.assertEquals(Umvc3Character.ZERO, team.getSecondCharacter());
        Assert.assertEquals(AssistType.ALPHA, team.getSecondAssistType());
        Assert.assertEquals(new Assist(AssistType.ALPHA, Umvc3Character.ZERO), team.getSecondAssist());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, team.getThirdCharacter());
        Assert.assertNull(team.getThirdAssistType());
        Assert.assertNull(team.getThirdAssist());
        
        Assert.assertEquals("Wolverine (gamma) / Zero (alpha) / Doctor Doom", team.getNameWithAssists());
        Assert.assertEquals("Wolverine (Berserker Barrage) / Zero (Ryuenjin) / Doctor Doom", team.getNameWithAssistMoveNames());
    }

    /** Tests what happens when we try to pass all null values to the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullValues() {
        new Team(null, null, null, null, null, null);
    }
}
