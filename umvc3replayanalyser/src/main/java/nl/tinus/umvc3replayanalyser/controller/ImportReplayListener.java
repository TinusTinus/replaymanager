package nl.tinus.umvc3replayanalyser.controller;

import java.util.List;

import nl.tinus.umvc3replayanalyser.model.Replay;

/**
 * Listener called when replays have been imported.
 * 
 * @author Martijn van de Rijdt
 */
// TODO This interface may be unnecessary.
// It could be replaced by turning Umvc3ReplayManagerController.replays into an observable list.
public interface ImportReplayListener {
    /**
     * Called when new replays have been imported.
     * 
     * @param replays
     *            newly imported replays
     */
    public void replaysImported(List<Replay> replays);
}
