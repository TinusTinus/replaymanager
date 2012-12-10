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
     * @param meaintainCharacterOrder
     *            whether character order needs to be respected when matching against a team
     */
    public MatchTeamPredicate(Umvc3Character character1, Umvc3Character character2, Umvc3Character character3,
            AssistType assist1, AssistType assist2, AssistType assist3, boolean maintainCharacterOrder) {
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
