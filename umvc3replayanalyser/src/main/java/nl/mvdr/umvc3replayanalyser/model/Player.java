// Generated by delombok at Sat Feb 08 23:15:03 CET 2014
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

import lombok.NonNull;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Player in an Ultimate Marvel vs Capcom 3 online match on XBox Live.
 *
 * @author Martijn van de Rijdt
 */
public class Player {
    // For now, a player will only contain a gamertag.
    // Later on this may be enriched with other information such as rank.
    /**
     * Gamertag.
     */
    @NonNull
    private final String gamertag;

    /**
     * Constructor.
     * 
     * @param gamertag
     *            gamertag
     */
    @JsonCreator
    public Player(@JsonProperty("gamertag") @NonNull String gamertag) {
        if (gamertag == null) {
            throw new java.lang.NullPointerException("gamertag");
        }
        this.gamertag = gamertag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.gamertag;
    }

    /**
     * Gamertag.
     */
    @NonNull
    @java.lang.SuppressWarnings("all")
    public String getGamertag() {
        return this.gamertag;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Player))
            return false;
        final Player other = (Player) o;
        if (!other.canEqual((java.lang.Object) this))
            return false;
        final java.lang.Object this$gamertag = this.getGamertag();
        final java.lang.Object other$gamertag = other.getGamertag();
        if (this$gamertag == null ? other$gamertag != null : !this$gamertag.equals(other$gamertag))
            return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    public boolean canEqual(final java.lang.Object other) {
        return other instanceof Player;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 277;
        int result = 1;
        final java.lang.Object $gamertag = this.getGamertag();
        result = result * PRIME + ($gamertag == null ? 0 : $gamertag.hashCode());
        return result;
    }
}