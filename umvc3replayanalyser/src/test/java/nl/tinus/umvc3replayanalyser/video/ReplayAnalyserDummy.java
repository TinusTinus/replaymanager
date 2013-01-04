package nl.tinus.umvc3replayanalyser.video;

/**
 * Dummy implementation of the replay analyser interface.
 * 
 * @author Martijn van de Rijdt
 */
public class ReplayAnalyserDummy implements ReplayAnalyser {
    /** {@inheritDoc} */
    @Override
    public GameAndVersusScreen analyse(String videoUrl) throws ReplayAnalysisException {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
