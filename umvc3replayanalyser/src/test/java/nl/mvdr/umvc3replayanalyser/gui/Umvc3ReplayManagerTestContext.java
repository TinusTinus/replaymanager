/*
 * Copyright 2013 Martijn van de Rijdt
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
package nl.mvdr.umvc3replayanalyser.gui;

import nl.mvdr.umvc3replayanalyser.gui.Umvc3ReplayManager;

/**
 * Main class, whose main method simply calls {@link Umvc3ReplayManager#main(String[])}.
 * 
 * Workaround for the fact that the test classpath (which contains useful things such as a log4j configuration) does not
 * seem to be available when running {@link Umvc3ReplayManager#main(String[])} directly in Eclipse Kepler.
 * 
 * @author Martijn van de Rijdt
 */
public class Umvc3ReplayManagerTestContext {

    /**
     * Starts {@link Umvc3ReplayManager#main(String[])}.
     * 
     * @param args
     *            command line parameters, which are passed on
     */
    public static void main(String[] args) {
        Umvc3ReplayManager.main(args);
    }
}
