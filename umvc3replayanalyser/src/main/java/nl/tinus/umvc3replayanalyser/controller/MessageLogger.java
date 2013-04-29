package nl.tinus.umvc3replayanalyser.controller;

/**
 * Interface for logging messages.
 * 
 * @author Martijn van de Rijdt
 */
interface MessageLogger {

    /**
     * Logs the message to a log file, and optionally also to the user interface so the user can read it.
     * 
     * @param message
     *            message to be logged
     */
    void log(String message);
}
