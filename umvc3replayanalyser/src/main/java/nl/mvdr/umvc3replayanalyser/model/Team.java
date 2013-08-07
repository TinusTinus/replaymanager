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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A team in Ultimate Marvel vs Capcom 3. Each match has two of these fighting each other.
 * 
 * @author Martijn van de Rijdt
 */
@Getter
@EqualsAndHashCode
public class Team {
    /** Point character. */
    @NonNull
    private final Umvc3Character firstCharacter;
    /** Assist type of the point character. May be null if unknown. */
    private final AssistType firstAssistType;
    /** Second character. */
    @NonNull
    private final Umvc3Character secondCharacter;
    /** Assist type of the scond character. May be null if unknown. */
    private final AssistType secondAssistType;
    /** Anchor. */
    @NonNull
    private final Umvc3Character thirdCharacter;
    /** Assist type of the anchor character. May be null if unknown. */
    private final AssistType thirdAssistType;

    /**
     * Convenience constructor, for when none of the assists are known.
     * 
     * @param firstCharacter
     *            point character
     * @param secondCharacter
     *            second character
     * @param thirdCharacter
     *            anchor
     */
    public Team(Umvc3Character firstCharacter, Umvc3Character secondCharacter, Umvc3Character thirdCharacter) {
        this(firstCharacter, null, secondCharacter, null, thirdCharacter, null);
    }

    /**
     * Constructor.
     * 
     * @param firstCharacter
     *            point character
     * @param secondCharacter
     *            second character
     * @param thirdCharacter
     *            anchor
     */
    @JsonCreator
    public Team(@JsonProperty("firstCharacter") @NonNull Umvc3Character firstCharacter,
            @JsonProperty("firstAssist") AssistType firstAssist,
            @JsonProperty("secondCharacter") @NonNull Umvc3Character secondCharacter,
            @JsonProperty("secondAssist") AssistType secondAssist,
            @JsonProperty("thirdCharacter") @NonNull Umvc3Character thirdCharacter,
            @JsonProperty("thirdAssist") AssistType thirdAssist) {
        super();
        
        this.firstCharacter = firstCharacter;
        this.firstAssistType = firstAssist;
        this.secondCharacter = secondCharacter;
        this.secondAssistType = secondAssist;
        this.thirdCharacter = thirdCharacter;
        this.thirdAssistType = thirdAssist;
    }

    /**
     * Returns an unmodifiable list containing all three characters.
     * 
     * @return characters
     */
    @JsonIgnore
    public List<Umvc3Character> getCharacters() {
        return Collections.unmodifiableList(Arrays.asList(firstCharacter, secondCharacter, thirdCharacter));
    }

    /**
     * Returns an unmodifiable list containing all three assist types.
     * 
     * @return assist types
     */
    @JsonIgnore
    public List<AssistType> getAssists() {
        return Collections.unmodifiableList(Arrays.asList(firstAssistType, secondAssistType, thirdAssistType));
    }

    /**
     * Returns a name for the team, based on which characters it contains. The name does not include information about
     * which assists are being used.
     * 
     * For most teams, the result's format is "Point Character / Second Character / Anchor". For example:
     * "Wolverine / Zero / Doctor Doom" or "Phoenix Wright / Viewtiful Joe / Dante".
     * 
     * Some teams, such as Vergil / Dante / Wesker and Zero / Dante / Vergil, have special names (for these examples,
     * "Team Trenchcoat" and "Zero May Die", respectively).
     * 
     * @return team name
     */
    @JsonIgnore
    public String getName() {
        String result;
        if (firstCharacter == Umvc3Character.VERGIL && secondCharacter == Umvc3Character.DANTE
                && thirdCharacter == Umvc3Character.WESKER) {
            result = "Team Trenchcoat";
        } else if (firstCharacter == Umvc3Character.ZERO && secondCharacter == Umvc3Character.DANTE
                && thirdCharacter == Umvc3Character.VERGIL) {
            result = "Zero May Cry";
        } else if (firstCharacter == Umvc3Character.MAGNETO && secondCharacter == Umvc3Character.STORM
                && thirdCharacter == Umvc3Character.SENTINEL) {
            result = "MSS";
        } else {
            result = String.format("%s / %s / %s", firstCharacter.getName(), secondCharacter.getName(),
                    thirdCharacter.getName());
        }
        return result;
    }

    /**
     * Returns a name for the team, including (if known) which assists are being used. Assists are represented by
     * "alpha", "beta" or "gamma".
     * 
     * The result's format is of "Point Character / Second Character / Anchor". For example:
     * "Wolverine (gamma) / Zero (alpha) / Doctor Doom (gamma)".
     * 
     * @return team name including assists
     */
    @JsonIgnore
    public String getNameWithAssists() {
        return getNameWithAssists(false);
    }

    /**
     * Returns a name for the team, including which assists are being used. Assists are represented by the move names.
     * 
     * The result's format is of "Point Character / Second Character / Anchor". For example:
     * "Wolverine (Berserker Barrage) / Zero (Ryuenjin) / Doctor Doom (Plasma Beam)".
     * 
     * @return team name including assists
     */
    @JsonIgnore
    public String getNameWithAssistMoveNames() {
        return getNameWithAssists(true);
    }

    /**
     * Returns a name for the team, including (if known) which assists are being used.
     * 
     * The result's format is of "Point Character / Second Character / Anchor". For example:
     * "Wolverine (gamma) / Zero (alpha) / Doctor Doom (gamma)".
     * 
     * @param useAssistNames
     *            if true, the assists are represented by the move names (for example: Hidden Missiles); otherwise by
     *            the greek letters (e.g. alpha)
     * 
     * @return team name including assists
     */
    @JsonIgnore
    public String getNameWithAssists(boolean useAssistNames) {
        String result;

        if (firstAssistType == null && secondAssistType == null && thirdAssistType == null) {
            result = getName();
        } else {
            String firstCharacterName = getCharacterNameWithAssist(firstCharacter, firstAssistType, useAssistNames);
            String secondCharacterName = getCharacterNameWithAssist(secondCharacter, secondAssistType, useAssistNames);
            String thirdCharacterName = getCharacterNameWithAssist(thirdCharacter, thirdAssistType, useAssistNames);
            result = String.format("%s / %s / %s", firstCharacterName, secondCharacterName, thirdCharacterName);
        }
        return result;
    }

    /**
     * Returns the given character's name with the assist in brackets if known.
     * 
     * @param character
     *            character
     * @param assist
     *            assist type; may be null if unknown
     * @param useAssistName
     *            if true, the assist is represented by the move name (for example: Hidden Missiles); otherwise by the
     *            greek letter (e.g. alpha)
     * @return String representation of the character and the assist
     */
    @JsonIgnore
    private String getCharacterNameWithAssist(Umvc3Character character, AssistType assist, boolean useAssistName) {
        String result = character.getName();
        if (assist != null) {
            String assistString;
            if (useAssistName) {
                assistString = character.getAssistName(assist);
            } else {
                assistString = assist.getName();
            }
            result = String.format("%s (%s)", result, assistString);
        }
        return result;
    }
    
    /** @return first assist; null if the assist type is unknown */
    @JsonIgnore
    public Assist getFirstAssist() {
        Assist result;
        if (firstAssistType != null) {
            result = new Assist(firstAssistType, firstCharacter);
        } else {
            result = null;
        }
        return result;
    }
    
    /** @return second assist; null if the assist type is unknown */
    @JsonIgnore
    public Assist getSecondAssist() {
        Assist result;
        if (secondAssistType != null) {
            result = new Assist(secondAssistType, secondCharacter);
        } else {
            result = null;
        }
        return result;
    }

    /** @return third assist; null if the assist type is unknown */
    @JsonIgnore
    public Assist getThirdAssist() {
        Assist result;
        if (thirdAssistType != null) {
            result = new Assist(thirdAssistType, thirdCharacter);
        } else {
            result = null;
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getNameWithAssists();
    }
}
