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
package nl.tinus.umvc3replayanalyser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Player in an Ultimate Marvel vs Capcom 3 online match on XBox Live.
 * 
 * @author Martijn van de Rijdt
 */
@Getter
@EqualsAndHashCode
public class Player {
    // For now, a player will only contain a gamertag.
    // Later on this may be enriched with other information such as rank.
    /** Gamertag. */
    @NonNull
    private final String gamertag;

    /**
     * Constructor.
     * 
     * @param gamertag
     *            gamertag
     */
    @JsonCreator
    public Player(@JsonProperty("gamertag") String gamertag) {
        super();
        this.gamertag = gamertag;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.gamertag;
    }
}
