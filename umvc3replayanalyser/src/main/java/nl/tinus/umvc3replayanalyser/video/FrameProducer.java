package nl.tinus.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IError;

/**
 * Produces video frames from a video file.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
class FrameProducer extends MediaListenerAdapter implements Callable<IError> {
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
    public IError call() {
        IMediaReader reader = ToolFactory.makeReader(this.videoUrl);
        reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        reader.addListener(this);

        IError error = null;
        while (error == null && !productionCanStop) {
            // Whenever readPacket results in a complete picture, it will trigger the onVideoPicture method.
            error = reader.readPacket();
            log.debug("Read packet.");
        }
        
        log.info("Done.");

        return error;
    }

    /** {@inheritDoc} */
    @Override
    public void onVideoPicture(IVideoPictureEvent event) {
        log.debug("Decoded picture for timestamp " + event.getTimeStamp());
        BufferedImage image = event.getImage();
        if (image == null) {
            log.warn("Buffered image not available for timestamp " + event.getTimeStamp());
        } else {
            boolean success = false;
            while (!success && !productionCanStop) {
                try {
                    success = queue.offer(image, 1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.error(
                            String.format("Skipping image with timestamp %s due to an unexpected exception.",
                                    event.getTimeStamp()), e);
                }
            }
        }
    }

    /** Indicates that the producer no longer needs to keep producing frames. */
    void stopProduction() {
        this.productionCanStop = true;
    }
}
