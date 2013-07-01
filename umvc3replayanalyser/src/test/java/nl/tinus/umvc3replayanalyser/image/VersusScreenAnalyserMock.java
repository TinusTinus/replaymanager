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
package nl.tinus.umvc3replayanalyser.image;

import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.model.Player;
import nl.tinus.umvc3replayanalyser.model.Team;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;
import nl.tinus.umvc3replayanalyser.ocr.OCRException;

/**
 * Mock implementation of {@link VersusScreenAnalyser}, for use in unit tests.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class VersusScreenAnalyserMock implements VersusScreenAnalyser {
    /** The dummy game returned by this mock's analyse method. */
    public static final Game DUMMY_GAME = new Game(new Player("Player One"), new Team(Umvc3Character.VERGIL,
            Umvc3Character.DANTE, Umvc3Character.TRISH), new Player("Player Two"), new Team(Umvc3Character.RYU,
            Umvc3Character.CHUN_LI, Umvc3Character.AKUMA));

    /** The number of times the analyse method has been called. */
    @Getter
    private int numberOfCalls;

    /** Constructor. */
    public VersusScreenAnalyserMock() {
        super();
        this.numberOfCalls = 0;
    }

    /** {@inheritDoc} */
    @Override
    public Game analyse(BufferedImage versusImage) throws OCRException {
        log.info("analyse called: " + versusImage);
        this.numberOfCalls++;
        return DUMMY_GAME;
    }

    /** {@inheritDoc} */
    @Override
    public boolean canBeVersusScreen(BufferedImage image) {
        return true;
    }
}
