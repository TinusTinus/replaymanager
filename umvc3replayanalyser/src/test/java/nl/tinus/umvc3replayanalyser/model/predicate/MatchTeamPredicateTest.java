package nl.tinus.umvc3replayanalyser.model.predicate;

import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MatchTeamPredicate.
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
