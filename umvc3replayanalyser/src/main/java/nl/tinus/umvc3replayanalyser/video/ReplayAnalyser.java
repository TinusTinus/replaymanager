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
import nl.tinus.umvc3replayanalyser.model.Game;

import com.xuggle.xuggler.IError;

/**
 * Analyses an Ultimate Marvel vs Capcom 3 replay.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
@RequiredArgsConstructor
public class ReplayAnalyser {
    /** Number of consumers. */
    // More than the number of cores in the machine seems pointless.
    private static final int NUM_CONSUMERS = 8;
    /** Capacity for the queue. */
    // Performance-wise this value does not seem to matter all that much.
    // Theoretically all consumers could be looking for a new frame, so choosing this value equal to the amount of
    // consumers seems like a sensible value. More than that is just a waste of memory.
    // In practice it does not seem to affect time performance that much even if set to 1 or 2, simply because the
    // producer is not the performance bottleneck at all.
    private static final int QUEUE_CAPACITY = NUM_CONSUMERS;
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
    public Game analyse(String videoUrl) throws ReplayAnalysisException {
        log.info("Analysing: " + videoUrl);
        
        long startTime = System.currentTimeMillis();

        Game result = null;

        // Create a producer, which reads the replay and produces individual frames as BufferedImages,
        // and a number of consumers, which process the images and attempt to read the Game information.
        BlockingQueue<BufferedImage> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_CONSUMERS + 1);

        try {
            List<FrameConsumer> consumers = new ArrayList<>(NUM_CONSUMERS);
            List<Future<Game>> consumerFutures = new ArrayList<>(NUM_CONSUMERS);
            for (int i = 0; i != NUM_CONSUMERS; i++) {
                FrameConsumer consumer = new FrameConsumer(versusScreenAnalyser, queue);
                consumers.add(consumer);
                Future<Game> future = executorService.submit(consumer);
                consumerFutures.add(future);
            }

            FrameProducer producer = new FrameProducer(videoUrl, queue);
            Future<IError> producerFuture = executorService.submit(producer);

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
                    Future<Game> future = consumerFutures.get(i);
                    if (future.isDone()) {
                        consumerFutures.remove(i);
                        consumers.remove(i);

                        try {
                            Game game = future.get();
                            if (game != null) {
                                result = game;
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
                        throw new ReplayAnalysisException(error);
                    } else {
                        throw new ReplayAnalysisException("Replay analysis failed.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    ReplayAnalysisException newException = new ReplayAnalysisException("Replay analysis failed.");
                    newException.addSuppressed(e);
                    throw newException;
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
    public Map<File, Game> analyse(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        Map<File, Game> result = new HashMap<>();

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                result.putAll(analyse(file));
            } else {
                try {
                    Game game = analyse(file.getAbsolutePath());
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
