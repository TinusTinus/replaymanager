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
package nl.tinus.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * Produces video frames from a video file.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
// TODO implement Runnable instead of Callable<Void>.
class FrameProducer implements Callable<Void> {
    /** URL of the video file to read frames from. */
    private final String videoUrl;

    /** Queue to place items into. */
    private final BlockingQueue<BufferedImage> queue;

    /** Indicates whether production is still needed. */
    private boolean productionCanStop;

    /**
     * Constructor.
     * 
     * @param videoUrl
     *            URL of the video file from which to produce frames
     * @param queue
     *            queue to place items into
     */
    FrameProducer(String videoUrl, BlockingQueue<BufferedImage> queue) {
        super();
        this.videoUrl = videoUrl;
        this.queue = queue;
        this.productionCanStop = false;
    }

    /**
     * Starts the producer.
     * 
     * @returns the IError that caused prodcution to be halted, or null if no such error occurred
     */
    @Override
    public Void call() {
        try (FrameIterator frameIter = new FrameIterator(this.videoUrl)) {
            while (!productionCanStop && frameIter.hasNext()) {
                BufferedImage image = frameIter.next();
                boolean success = false;
                while (!success && !productionCanStop) {
                    try {
                        success = queue.offer(image, 1, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        log.error("Failed to offer image to queue due to an unexpected exception: " + image, e);
                    }
                }
            }
            log.info("Done.");
        }
        return null;
    }

    /** Indicates that the producer no longer needs to keep producing frames. */
    void stopProduction() {
        this.productionCanStop = true;
    }
}
