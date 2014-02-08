// Generated by delombok at Sat Feb 08 23:17:45 CET 2014
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
package nl.mvdr.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.model.Player;
import nl.mvdr.umvc3replayanalyser.model.Team;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

/**
 * Mock implementation of {@link VersusScreenAnalyser}, for use in unit tests.
 *
 * @author Martijn van de Rijdt
 */
public class VersusScreenAnalyserMock implements VersusScreenAnalyser {
    @java.lang.SuppressWarnings("all")
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VersusScreenAnalyserMock.class);
    
    /**
     * The dummy game returned by this mock's analyse method.
     */
    public static final Game DUMMY_GAME = new Game(new Player("Player One"), new Team(Umvc3Character.VERGIL, Umvc3Character.DANTE, Umvc3Character.TRISH), new Player("Player Two"), new Team(Umvc3Character.RYU, Umvc3Character.CHUN_LI, Umvc3Character.AKUMA));
    
    /**
     * The number of times the analyse method has been called.
     */
    private int numberOfCalls;

    /**
     * Constructor.
     */
    public VersusScreenAnalyserMock() {
        this.numberOfCalls = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game analyse(BufferedImage versusImage) {
        log.info("analyse called: " + versusImage);
        this.numberOfCalls++;
        return DUMMY_GAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canBeVersusScreen(BufferedImage image) {
        return true;
    }

    /**
     * The number of times the analyse method has been called.
     */
    @java.lang.SuppressWarnings("all")
    public int getNumberOfCalls() {
        return this.numberOfCalls;
    }
}