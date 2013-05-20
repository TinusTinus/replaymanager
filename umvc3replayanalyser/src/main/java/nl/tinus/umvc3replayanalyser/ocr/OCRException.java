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
package nl.tinus.umvc3replayanalyser.ocr;

/**
 * Indicates that something has gone wrong while attempting optical character recognition.
 * 
 * @author Martijn van de Rijdt
 */
public class OCRException extends Exception {

    /** Generated. */
    private static final long serialVersionUID = -3433722707352162943L;

    /** Constructor. */
    public OCRException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param message message
     */
    public OCRException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param cause cause
     */
    public OCRException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * 
     * @param message message
     * @param cause cause
     */
    public OCRException(String message, Throwable cause) {
        super(message, cause);
    }
}
