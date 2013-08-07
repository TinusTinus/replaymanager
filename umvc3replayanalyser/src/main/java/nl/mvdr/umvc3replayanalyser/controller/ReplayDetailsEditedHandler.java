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
package nl.mvdr.umvc3replayanalyser.controller;

import nl.mvdr.umvc3replayanalyser.model.Game;

/**
 * Interface for handling the event that the user has completed the edit replay details popup window.
 * 
 * @author Martijn van de Rijdt
 */
interface ReplayDetailsEditedHandler {
    /**
     * Handles the event that the user has completed (not cancelled) the edit replay details popup window.
     * 
     * @param game
     *            contains the game details
     */
    void handleReplayDetailsEdited(Game game);
}
