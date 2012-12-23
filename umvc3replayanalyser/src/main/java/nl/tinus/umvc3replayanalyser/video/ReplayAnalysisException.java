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

    /**
     * Returns an error message based on the given cause. If any of the exceptions in the given throwable's cause chain
     * are a ReplayAnalysisException as well, the error message of that exception will be copied over. Otherwise a
     * generic exception message is returned.
     * 
     * @param throwable
     *            throwable whose cause chain is to be examined
     * 
     * @return exception message
     */
    private static String getMessage(Throwable throwable) {
        String result = null;
        Throwable t = throwable;
        while (result == null && t != null) {
            if (t instanceof ReplayAnalysisException) {
                result = t.getMessage();
            }
            t = t.getCause();
        }
        
        if (result == null) {
            result = "Replay analysis failed.";
        }
        
        return result;
    }
    
    /** Constructor. */
    public ReplayAnalysisException() {
        super();
        xuggleError = null;
    }
    
    /**
     * Constructor.
     * 
     * @param cause
     *            cause
     */
    public ReplayAnalysisException(Throwable cause) {
        super(getMessage(cause), cause);
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
