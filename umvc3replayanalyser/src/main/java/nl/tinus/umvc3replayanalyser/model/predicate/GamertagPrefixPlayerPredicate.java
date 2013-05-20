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
package nl.tinus.umvc3replayanalyser.model.predicate;

import org.apache.commons.lang3.StringUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.tinus.umvc3replayanalyser.model.Player;

import com.google.common.base.Predicate;

/**
 * Predicate that checks whether the player's gamertag starts with the given string.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor
@ToString
public class GamertagPrefixPlayerPredicate implements Predicate<Player> {
    /** Name prefix to be matched. */
    @NonNull
    private final String prefix;
    
    /** {@inheritDoc} */
    @Override
    public boolean apply(Player player) {
        return StringUtils.startsWithIgnoreCase(player.getGamertag(), prefix);
    }
}
