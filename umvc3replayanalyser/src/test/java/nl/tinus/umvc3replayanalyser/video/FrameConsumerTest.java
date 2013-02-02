package nl.tinus.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.imageio.ImageIO;

import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyserMock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for FrameConsumer.
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
        this.queue = new ArrayBlockingQueue<BufferedImage>(10);
        this.versusScreenAnalyser = new VersusScreenAnalyserMock();
        this.frameConsumer = new FrameConsumer(versusScreenAnalyser, this.queue);
        this.frameConsumer.producerStopped();
    }

    /**
     * Tests the canBeVersusScreen method with an image that actually is a versus screen.
     * 
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testCanBeVersusScreenTrue() throws IOException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vs.png"));
        Assert.assertTrue(frameConsumer.canBeVersusScreen(image));
    }

    /**
     * Tests the canBeVersusScreen method with an image that cannot be a versus screen.
     * 
     * @throws IOException
     *             unexpected
     */
    @Test
    public void testCanBeVersusScreenFalse() throws IOException {
        BufferedImage image = ImageIO.read(new File("src/test/resources/vsinverted.png"));
        Assert.assertFalse(frameConsumer.canBeVersusScreen(image));
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
    
    /**
     * Tests the call method.
     * 
     * @throws IOException
     *             unexpected
     * @throws InterruptedException
     *             unexpected
     */
    @Test
    public void testCallOnlyNotVersusScreen() throws IOException, InterruptedException {
        BufferedImage notVersusScreen = ImageIO.read(new File("src/test/resources/vsinverted.png"));
        this.queue.put(notVersusScreen);
        this.queue.put(notVersusScreen);
        this.queue.put(notVersusScreen);
        
        GameAndVersusScreen result = this.frameConsumer.call();
        
        Assert.assertNull(result);
        Assert.assertEquals(0, versusScreenAnalyser.getNumberOfCalls());
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
    public void testCallMixed() throws IOException, InterruptedException {
        BufferedImage versusScreen = ImageIO.read(new File("src/test/resources/vs.png"));
        BufferedImage anotherVersusScreen = ImageIO.read(new File("src/test/resources/vs.png"));
        BufferedImage notVersusScreen = ImageIO.read(new File("src/test/resources/vsinverted.png"));
        this.queue.put(notVersusScreen);
        this.queue.put(notVersusScreen);
        this.queue.put(versusScreen);
        this.queue.put(anotherVersusScreen);
        this.queue.put(notVersusScreen);
        
        GameAndVersusScreen result = this.frameConsumer.call();
        
        Assert.assertNotNull(result);
        Assert.assertSame(versusScreen, result.getVersusScreen());
        Assert.assertSame(VersusScreenAnalyserMock.DUMMY_GAME, result.getGame());
        Assert.assertEquals(1, versusScreenAnalyser.getNumberOfCalls());
    }
}
