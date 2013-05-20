package nl.tinus.umvc3replayanalyser.controller;

import java.io.IOException;

/** Exception class indicating that a path cannot be resolved. */
class PathResolutionException extends IOException {
    /** Serial version UID. */
    private static final long serialVersionUID = 8299984220960706361L;

    /**
     * Constructor.
     * 
     * @param message
     *            exception message
     */
    PathResolutionException(String message) {
        super(message);
    }
}