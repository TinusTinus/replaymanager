package nl.tinus.umvc3replayanalyser.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Analyses an Ultimate Marvel vs Capcom 3 replay.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor
public class ReplayAnalyser {
    /** Time to wait in between polling attempts, in milliseconds. */
    private static final long TIME_BETWEEN_POLLS = 100;

    /** Versus screen analyser. */
    private final VersusScreenAnalyser versusScreenAnalyser;

    /**
     * Analyses the replay.
     * 
     * @return game
     * @throws ReplayAnalysisException
     *             in case analysis fails
     */
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

            producer.stopProduction();
            for (FrameConsumer consumer : consumers) {
                consumer.stopConsumption();
            }

            if (result == null) {
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
                    throw new ReplayAnalysisException("Replay analysis failed.", e);
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

    /**
     * Recursively analyses all of the files in the given directory.
     * 
     * @param directory
     *            directory
     * @return map of succesfully analysed files to their game information
     */
    public Map<File, GameAndVersusScreen> analyse(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        Map<File, GameAndVersusScreen> result = new HashMap<>();

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                result.putAll(analyse(file));
            } else {
                try {
                    GameAndVersusScreen game = analyse(file.getAbsolutePath());
                    result.put(file, game);
                } catch (ReplayAnalysisException e) {
                    log.warn("Replay could not be analysed: " + file, e);
                }
            }
        }
        
        log.info("Analysed directory: " + result);

        return result;
    }
}
