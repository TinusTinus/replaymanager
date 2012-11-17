package nl.tinus.umvc3replayanalyser.model;

import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Side;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Game.
 * 
 * @author Martijn van de Rijdt
 */
public class GameTest {
    /** Tests the constructor and a bunch of methods in the Game class. */
    @Test
    public void test() {
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus, Side.PLAYER_ONE);

        Assert.assertEquals(Side.PLAYER_TWO, game.getLosingSide());
        Assert.assertEquals(djAlbertoLara, game.getWinningPlayer());
        Assert.assertEquals(tinus, game.getLosingPlayer());
        Assert.assertEquals("DJ Alberto Lara (Hulk / Wolverine / Sentinel) vs. MvdR (Wolverine / Zero / Doctor Doom)",
                game.getDescription(false, false));
        Assert.assertEquals(
                "DJ Alberto Lara (Hulk / Wolverine / Sentinel) (W) vs. MvdR (Wolverine / Zero / Doctor Doom)",
                game.getDescription(false, true));
        Assert.assertEquals(
                "DJ Alberto Lara (Hulk (Gamma Wave) / Wolverine (Berserker Slash) / Sentinel (Sentinel Force Charge)) vs. MvdR (Wolverine (Berserker Barrage) / Zero (Ryuenjin) / Doctor Doom (Plasma Beam))",
                game.getDescription(true, false));
        Assert.assertEquals(
                "DJ Alberto Lara (Hulk (Gamma Wave) / Wolverine (Berserker Slash) / Sentinel (Sentinel Force Charge)) (W) vs. MvdR (Wolverine (Berserker Barrage) / Zero (Ryuenjin) / Doctor Doom (Plasma Beam))",
                game.getDescription(true, true));
    }

    /** Tests the equals method. */
    @Test
    public void testNotEqual() {
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game1 = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus, Side.PLAYER_ONE);

        Player badhyper = new Player("badhyper");
        Team teamBadhyper = new Team(Umvc3Character.FELICIA, Umvc3Character.MODOK, Umvc3Character.WESKER);
        Player nemo = new Player("Nemo");
        Team teamNemo = new Team(Umvc3Character.NOVA, Umvc3Character.SPENCER, Umvc3Character.DOCTOR_STRANGE);
        Game game2 = new Game(badhyper, teamBadhyper, nemo, teamNemo);

        Assert.assertFalse(game1.equals(game2));
        Assert.assertFalse(game2.equals(game1));
    }

    /** Tests the equals method. */
    @Test
    public void testEqualSame() {
        Player djAlbertoLara = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus = new Player("MvdR");
        Team teamTinus = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game = new Game(djAlbertoLara, teamDjAlbertoLara, tinus, teamTinus, Side.PLAYER_ONE);

        Assert.assertEquals(game, game);
    }

    /** Tests the equals method. */
    @Test
    public void testEquals() {
        Player djAlbertoLara1 = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara1 = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus1 = new Player("MvdR");
        Team teamTinus1 = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game1 = new Game(djAlbertoLara1, teamDjAlbertoLara1, tinus1, teamTinus1, Side.PLAYER_ONE);

        Player djAlbertoLara2 = new Player("DJ Alberto Lara");
        Team teamDjAlbertoLara2 = new Team(Umvc3Character.HULK, AssistType.ALPHA, Umvc3Character.WOLVERINE,
                AssistType.BETA, Umvc3Character.SENTINEL, AssistType.ALPHA);
        Player tinus2 = new Player("MvdR");
        Team teamTinus2 = new Team(Umvc3Character.WOLVERINE, AssistType.GAMMA, Umvc3Character.ZERO, AssistType.ALPHA,
                Umvc3Character.DOCTOR_DOOM, AssistType.ALPHA);
        Game game2 = new Game(djAlbertoLara2, teamDjAlbertoLara2, tinus2, teamTinus2, Side.PLAYER_ONE);

        Assert.assertEquals(game1, game2);
    }
}
