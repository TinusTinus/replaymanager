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
package nl.mvdr.umvc3replayanalyser.controller;

import java.io.File;
import java.util.Collections;

import nl.mvdr.umvc3replayanalyser.config.ConfigurationDummy;
import nl.mvdr.umvc3replayanalyser.model.Replay;
import nl.mvdr.umvc3replayanalyser.video.ReplayAnalyserDummy;

import org.junit.Test;

/**
 * Test class for {@link ImportReplayTask}.
 * 
 * @author Martijn van de Rijdt
 */
public class ImportReplayTaskTest {
    /** Tests the constructor. */
    @Test
    public void testConstructor() {
        new ImportReplayTask(new File("."), Collections.<Replay> emptyList(), new ReplayAnalyserDummy(),
                createReplaySaver());
    }

    /** Tests the constructor. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullDirectory() {
        new ImportReplayTask(null, Collections.<Replay> emptyList(), new ReplayAnalyserDummy(), createReplaySaver());
    }

    /** Tests the constructor. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullReplays() {
        new ImportReplayTask(new File("."), null, new ReplayAnalyserDummy(), createReplaySaver());
    }

    /** Tests the constructor. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullAnalyser() {
        new ImportReplayTask(new File("."), Collections.<Replay> emptyList(), null, createReplaySaver());
    }

    /** Tests the constructor. */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullReplaySaver() {
        new ImportReplayTask(new File("."), Collections.<Replay> emptyList(), new ReplayAnalyserDummy(), null);
    }

    /** Tests the constructor. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNotADirectory() {
        new ImportReplayTask(new File("derp"), Collections.<Replay> emptyList(), new ReplayAnalyserDummy(),
                createReplaySaver());
    }

    /**
     * Creates a replay saver.
     * 
     * @return replay saver
     */
    private ReplaySaver createReplaySaver() {
        ConfigurationDummy configuration = new ConfigurationDummy() {
            /** {@inheritDoc} */
            @Override
            public boolean isPrettyPrintReplays() {
                return false;
            }
        };
        return new ReplaySaver(configuration);
    }
}
