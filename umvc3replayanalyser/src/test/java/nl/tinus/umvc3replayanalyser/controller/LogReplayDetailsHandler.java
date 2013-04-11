package nl.tinus.umvc3replayanalyser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Game;

/**
 * Handler which only logs the given game.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor
class LogReplayDetailsHandler implements ReplayDetailsEditedHandler {
    /** {@inheritDoc} */
    @Override
    public void handleReplayDetailsEdited(Game game) {
        log.info("Game: " + game);
    }
}
