package nl.tinus.umvc3replayanalyser.video;

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
class FrameConsumer implements Callable<Game> {
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
    public Game call() throws InterruptedException {
        Game game = null;
        while (game == null && !consumptionCanStop && !(producerStopped && queue.isEmpty())) {
            BufferedImage image = queue.poll();
            if (image != null) {
                try {
                    game = versusScreenAnalyser.analyse(image);
                } catch (OCRException e) {
                    log.debug("Could not analyse frame. This frame is most likely not the versus screen.", e);
                }
            } else {
                Thread.sleep(WAIT_TIME_BETWEEN_POLLS);
            }
        }
        
        log.info("Done.");
        
        return game;
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
