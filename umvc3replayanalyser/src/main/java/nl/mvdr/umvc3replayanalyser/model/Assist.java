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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Representation of an assist for a specific character.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Assist {
    /** Assist type. */
    @NonNull
    private final AssistType type;
    /** Assist character. */
    @NonNull
    private final Umvc3Character character;
    
    /**
     * Gets the assist type, while handling null values.
     * 
     * @param assist
     *            assist, may be null
     * @return assist type, or null if the given assist is null
     */
    public static AssistType getType(Assist assist) {
        AssistType result;
        if (assist == null) {
            result = null;
        } else {
            result = assist.getType();
        }
        return result;
    }
    
    /** @return name of the assist */
    public String getName() {
        return character.getAssistName(type);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getName();
    }
}
