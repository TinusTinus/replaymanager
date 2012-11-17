package nl.tinus.umvc3replayanalyser.model;

import lombok.extern.slf4j.Slf4j;

import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Team.
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
    public void testNullValueCharacter() {
        new Team(null, Umvc3Character.ZERO, Umvc3Character.DOCTOR_DOOM);
    }

    
    /** Tests what happens when we try to pass a null assist value to the constructor. */
    @Test
    public void testNullValueAssist() {
        Team team = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA,
                Umvc3Character.ZERO, null,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        
        Assert.assertEquals(Umvc3Character.WOLVERINE, team.getFirstCharacter());
        Assert.assertEquals(AssistType.GAMMA, team.getFirstAssist());
        Assert.assertEquals(Umvc3Character.ZERO, team.getSecondCharacter());
        Assert.assertNull(team.getSecondAssist());
        Assert.assertEquals(Umvc3Character.DOCTOR_DOOM, team.getThirdCharacter());
        Assert.assertEquals(AssistType.ALPHA, team.getThirdAssist());
        
        Assert.assertEquals("Wolverine (gamma) / Zero / Doctor Doom (alpha)", team.getNameWithAssists());
        Assert.assertEquals("Wolverine (Berserker Barrage) / Zero / Doctor Doom (Plasma Beam)", team.getNameWithAssistMoveNames());
    }

    /** Tests what happens when we try to pass all null values to the constructor. */
    @Test(expected = NullPointerException.class)
    public void testNullValues() {
        new Team(null, null, null, null, null, null);
    }
}
