package nl.tinus.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.tinus.umvc3replayanalyser.image.VersusScreenAnalyser;

import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IError.Type;

/**
 * Implementation of the ReplayAnalyser interface.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor
public class ReplayAnalyserImpl implements ReplayAnalyser {
    /** Time to wait in between polling attempts, in milliseconds. */
    private static final long TIME_BETWEEN_POLLS = 100;

    /** Versus screen analyser. */
    private final VersusScreenAnalyser versusScreenAnalyser;

    /** {@inheritDoc} */
    @Override
    public GameAndVersusScreen analyse(String videoUrl) throws ReplayAnalysisException {
        log.info("Analysing: " + videoUrl);
        
        long startTime = System.currentTimeMillis();

        GameAndVersusScreen result = null;

        // Create a producer, which reads the replay and produces individual frames as BufferedImages,
        // and a number of consumers, which process the images and attempt to read the Game information.
        int numConsumers = Runtime.getRuntime().availableProcessors();
        int queueCapacity = numConsumers;
        BlockingQueue<BufferedImage> queue = new ArrayBlockingQueue<>(queueCapacity);
        ExecutorService executorService = Executors.newFixedThreadPool(numConsumers + 1);

        try {
            FrameProducer producer = new FrameProducer(videoUrl, queue);
            Future<IError> producerFuture = executorService.submit(producer);
            
            List<FrameConsumer> consumers = new ArrayList<>(numConsumers);
            List<Future<GameAndVersusScreen>> consumerFutures = new ArrayList<>(numConsumers);
            while(consumers.size() != numConsumers) {
                FrameConsumer consumer = new FrameConsumer(versusScreenAnalyser, queue);
                consumers.add(consumer);
                Future<GameAndVersusScreen> future = executorService.submit(consumer);
                consumerFutures.add(future);
            }

            // Continue until either a result has been found and/or all consumers are done.
            // As soon as a consumer is done both it and its future are removed from the lists.
            while (result == null && !consumerFutures.isEmpty()) {
                try {
                    Thread.sleep(TIME_BETWEEN_POLLS);
                } catch (InterruptedException e) {
                    throw new ReplayAnalysisException(e);
                }

                // Check if any of the consumers are done.
                int i = 0;
                while (i != consumerFutures.size()) {
                    Future<GameAndVersusScreen> future = consumerFutures.get(i);
                    if (future.isDone()) {
                        consumerFutures.remove(i);
                        consumers.remove(i);

                        try {
                            GameAndVersusScreen gameAndVersusScreen = future.get();
                            if (gameAndVersusScreen != null) {
                                result = gameAndVersusScreen;
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            log.error("Consumer failed.", e);
                        }

                    } else {
                        i++;
                    }
                }

                // Check if the producer is done.
                if (producerFuture.isDone()) {
                    for (FrameConsumer consumer : consumers) {
                        consumer.producerStopped();
                    }
                }
            }

            // A result has been found and/or all consumers are done.
            // Signal any active workers that they can stop their computation.
            producer.stopProduction();
            for (FrameConsumer consumer : consumers) {
                consumer.stopConsumption();
            }

            if (result == null) {
                // No game found. Throw a ReplayAnalysisException with meaningful info.
                try {
                    IError error = producerFuture.get();
                    if (error != null) {
                        String message;
                        if (Type.ERROR_EOF == error.getType()) {
                            message = "Replay analysis failed. No versus screen found in the video file.";
                        } else {
                            message = "Replay analysis failed. Xuggle error: " + error + ", type: " + error.getType();
                        }
                        
                        throw new ReplayAnalysisException(message, error);
                    } else {
                        throw new ReplayAnalysisException("Replay analysis failed.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new ReplayAnalysisException(e);
                }
            }
            
            // Make sure the producer has terminated, so we can be sure it has closed its reference to the video file.
            try {
                producerFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                // Whatever, we have our answer.
                if (log.isDebugEnabled()) {
                    log.debug("Producer threw an exception, but the game was still analysed succesfully.", e);
                }
            }
        } finally {
            executorService.shutdown();
        }

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        log.info(String.format("Game analysed: %s (time spent: %s ms)", result, "" + timeTaken));

        return result;
    }
}
