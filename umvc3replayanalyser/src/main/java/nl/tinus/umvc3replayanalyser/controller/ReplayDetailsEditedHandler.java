package nl.tinus.umvc3replayanalyser.controller;

import nl.tinus.umvc3replayanalyser.model.Game;

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
