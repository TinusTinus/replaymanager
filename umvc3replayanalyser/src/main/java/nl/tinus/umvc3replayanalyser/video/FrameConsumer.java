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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyser;
import nl.tinus.umvc3replayanalyser.model.Game;
import nl.tinus.umvc3replayanalyser.ocr.OCRException;

/**
 * Consumes and analyses individual video frames, until it succesfully finds and analyses a versus screen.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class FrameConsumer implements Callable<GameAndVersusScreen> {
    /** Wait time between polling attempts when no new frame is available, in milliseconds. */
    private static final long WAIT_TIME_BETWEEN_POLLS = 100;
    /** Margin of error when matching background colour. */
    private static final int COLOR_MARGIN = 50;

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
                if (canBeVersusScreen(image)) {
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

    /**
     * Checks if the given image is a candidate to be a versus screen.
     * 
     * The versus screen is mostly black. This method checks some of the pixels that are supposed to be black; if any of
     * them contains a different colour, this method throws an OCRException indicating that the given image is not a
     * versus screen.
     * 
     * @param image
     *            image to be checked
     * @return true if the image could be a versus screen, false if it definitely is not
     */
    // Default visibility for unit tests.
    // TODO to be able to support different resolutions, replace the absolute values by percentages of the image size
    boolean canBeVersusScreen(BufferedImage image) {
        return checkBlackPixel(image, 200, 60) && checkBlackPixel(image, 1080, 60) && checkBlackPixel(image, 200, 680)
                && checkBlackPixel(image, 1080, 680);
    }

    /**
     * Checks if the given pixel in the given image is black.
     * 
     * @param image
     *            image to be checked
     * @param x
     *            horizontal coordinate
     * @param y
     *            vertical coordinate
     * @return whether the pixel is black
     */
    private boolean checkBlackPixel(BufferedImage image, int x, int y) {
        Color color = new Color(image.getRGB(x, y));
        return equalsWithinMargin(color, Color.BLACK);
    }

    /**
     * Checks that the given colours are equal, within the allowed margin for error.
     * 
     * @param left
     *            left value
     * @param right
     *            right value
     * @return whether left and right are equal within the margin for error
     */
    private boolean equalsWithinMargin(Color left, Color right) {
        return equalsWithinMargin(left.getRed(), right.getRed())
                && equalsWithinMargin(left.getGreen(), right.getGreen())
                && equalsWithinMargin(left.getBlue(), right.getBlue());
    }

    /**
     * Checks that the absoloute difference between left and right is at most COLOR_MARGIN.
     * 
     * @param left
     *            left value
     * @param right
     *            right value
     * @return whether left and right are equal within the margin for error
     */
    private boolean equalsWithinMargin(int left, int right) {
        return Math.abs(left - right) <= COLOR_MARGIN;
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
