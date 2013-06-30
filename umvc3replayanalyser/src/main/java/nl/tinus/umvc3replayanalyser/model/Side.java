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

import lombok.AllArgsConstructor;

/**
 * Representation of the two sides in a game.
 * 
 * @author Martijn van de Rijdt
 */
@AllArgsConstructor
public enum Side {
    /** Player one. */
    PLAYER_ONE("Player One"),
    /** Player two. */
    PLAYER_TWO("Player Two");
    
    /** Name. */
    private final String name;
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.name;
    }
    
    /** @return the opposite side */
    public Side getOpposite() {
        Side result;
        if (this == PLAYER_ONE) {
            result = PLAYER_TWO;
        } else if (this == PLAYER_TWO) {
            result = PLAYER_ONE;
        } else {
            throw new IllegalStateException("Unexpected side: " + this);
        }
        return result;
    }
}
