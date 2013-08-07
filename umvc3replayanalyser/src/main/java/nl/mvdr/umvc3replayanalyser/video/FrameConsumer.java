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
package nl.mvdr.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.mvdr.umvc3replayanalyser.model.Game;
import nl.mvdr.umvc3replayanalyser.ocr.OCRException;

/**
 * Consumes and analyses individual video frames, until it succesfully finds and analyses a versus screen.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class FrameConsumer implements Callable<GameAndVersusScreen> {
    /** Wait time between polling attempts when no new frame is available, in milliseconds. */
    private static final long WAIT_TIME_BETWEEN_POLLS = 100;

    /** Versus screen analyser. */
    private final VersusScreenAnalyser versusScreenAnalyser;
    /** Queue to read frames from. */
    private final BlockingQueue<BufferedImage> queue;
    /** Indicates that the producer is no longer producing new items. */
    private boolean producerStopped;
    /** Indicates whether consumption is still needed. */
    private boolean consumptionCanStop;

    /**
     * Constructor.
     * 
     * @param versusScreenAnalyser
     *            versus screen analyser
     * @param queue
     *            queue
     */
    FrameConsumer(VersusScreenAnalyser versusScreenAnalyser, BlockingQueue<BufferedImage> queue) {
        super();
        this.versusScreenAnalyser = versusScreenAnalyser;
        this.queue = queue;
        this.producerStopped = false;
    }

    /** {@inheritDoc} */
    @Override
    public GameAndVersusScreen call() throws InterruptedException {
        GameAndVersusScreen result = null;
        while (result == null && !consumptionCanStop && !(producerStopped && queue.isEmpty())) {
            BufferedImage image = queue.poll();
            if (image != null) {
                if (versusScreenAnalyser.canBeVersusScreen(image)) {
                    try {
                        Game game = versusScreenAnalyser.analyse(image);
                        result = new GameAndVersusScreen(game, image);
                    } catch (OCRException e) {
                        if (log.isDebugEnabled()) {
                            log.debug("Could not analyse frame. This frame is most likely not the versus screen.", e);
                        }
                    }
                }
            } else {
                Thread.sleep(WAIT_TIME_BETWEEN_POLLS);
            }
        }

        log.info("Done.");

        return result;
    }

    /** Indicates that the producer is no longer producing items. */
    void producerStopped() {
        this.producerStopped = true;
    }

    /** Indicates that consumption of items is no longer needed. */
    void stopConsumption() {
        this.consumptionCanStop = true;
    }
}
