package nl.tinus.umvc3replayanalyser.video;

import lombok.Getter;

import com.xuggle.xuggler.IError;

/**
 * Exception indicating that replay analysis did not complete succesfully.
 * 
 * @author Martijn van de Rijdt
 */
public class ReplayAnalysisException extends Exception {
    /** Generated. */
    private static final long serialVersionUID = 1142007602702569328L;

    /** Error returned by Xuggle. */
    @Getter
    private final IError xuggleError;

    /** Constructor. */
    public ReplayAnalysisException() {
        super();
        xuggleError = null;
    }

    /** Constructor. */
    public ReplayAnalysisException(IError error) {
        this("Replay analysis failed. Xuggle error: " + error, error);
    }

    /**
     * Constructor.
     * 
     * @param message
     *            message
     */
    public ReplayAnalysisException(String message) {
        super(message);
        this.xuggleError = null;
    }

    /**
     * Constructor.
     * 
     * @param message
     *            message
     * @param error
     *            the xuggle error that resulted in this exception
     */
    public ReplayAnalysisException(String message, IError error) {
        super(message);
        this.xuggleError = error;
    }

    /**
     * Constructor.
     * 
     * @param cause
     *            cause
     */
    public ReplayAnalysisException(Throwable cause) {
        super(cause);
        this.xuggleError = null;
    }

    /**
     * Constructor.
     * 
     * @param message
     *            message
     * @param cause
     *            cause
     */
    public ReplayAnalysisException(String message, Throwable cause) {
        super(message, cause);
        this.xuggleError = null;
    }
}
