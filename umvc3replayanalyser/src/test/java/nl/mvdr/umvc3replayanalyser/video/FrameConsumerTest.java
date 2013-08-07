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
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.imageio.ImageIO;

import nl.mvdr.umvc3replayanalyser.image.VersusScreenAnalyserMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link FrameConsumer}.
 * 
 * @author Martijn van de Rijdt
 */
public class FrameConsumerTest {
    /** Object to be tested. */
    private FrameConsumer frameConsumer;

    /** Queue which can be used to pass data to the consumer. */
    private BlockingQueue<BufferedImage> queue;
    
    /** Mock versus screen analyser. */
    private VersusScreenAnalyserMock versusScreenAnalyser;

    /** Setup method. */
    @Before
    public void setUp() {
        this.queue = new ArrayBlockingQueue<>(10);
        this.versusScreenAnalyser = new VersusScreenAnalyserMock();
        this.frameConsumer = new FrameConsumer(versusScreenAnalyser, this.queue);
        this.frameConsumer.producerStopped();
    }

    /**
     * Tests the call method.
     * 
     * @throws IOException
     *             unexpected
     * @throws InterruptedException
     *             unexpected
     */
    @Test
    public void testCallOnlyVersusScreen() throws IOException, InterruptedException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vs.png"));
        this.queue.put(image);
        
        GameAndVersusScreen result = this.frameConsumer.call();
        
        Assert.assertNotNull(result);
        Assert.assertSame(image, result.getVersusScreen());
        Assert.assertSame(VersusScreenAnalyserMock.DUMMY_GAME, result.getGame());
        Assert.assertEquals(1, versusScreenAnalyser.getNumberOfCalls());
    }
    
    /**
     * Tests the call method.
     * 
     * @throws IOException
     *             unexpected
     * @throws InterruptedException
     *             unexpected
     */
    @Test
    public void testCallNoFrames() throws InterruptedException {
        GameAndVersusScreen result = this.frameConsumer.call();
        Assert.assertNull(result);
    }
    
    /**
     * Tests the call method.
     * 
     * @throws IOException
     *             unexpected
     * @throws InterruptedException
     *             unexpected
     */
    @Test
    public void testCallOnlyVersusScreenMultipleTimes() throws IOException, InterruptedException {
        BufferedImage versusScreen = ImageIO.read(new File("src/test/resources/vs.png"));
        this.queue.put(versusScreen);
        this.queue.put(versusScreen);
        this.queue.put(versusScreen);
        
        GameAndVersusScreen result = this.frameConsumer.call();
        
        Assert.assertNotNull(result);
        Assert.assertSame(versusScreen, result.getVersusScreen());
        Assert.assertSame(VersusScreenAnalyserMock.DUMMY_GAME, result.getGame());
        Assert.assertEquals(1, versusScreenAnalyser.getNumberOfCalls());
    }
}
