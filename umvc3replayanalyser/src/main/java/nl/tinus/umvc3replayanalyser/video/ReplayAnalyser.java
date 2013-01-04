package nl.tinus.umvc3replayanalyser.video;

/**
 * Analyses an Ultimate Marvel vs Capcom 3 replay.
 * 
 * @author Martijn van de Rijdt
 */
public interface ReplayAnalyser {
    /**
     * Analyses a video file.
     * 
     * @param videoUrl
     *            url of the video file
     * @return game
     * @throws ReplayAnalysisException
     *             in case analysis fails
     */
    GameAndVersusScreen analyse(String videoUrl) throws ReplayAnalysisException;
}
