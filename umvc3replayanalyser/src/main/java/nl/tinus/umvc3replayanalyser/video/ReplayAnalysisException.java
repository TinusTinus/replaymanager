package nl.tinus.umvc3replayanalyser.video;

/**
 * Exception indicating that replay analysis did not complete succesfully.
 * 
 * @author Martijn van de Rijdt
 */
public class ReplayAnalysisException extends Exception {
    /** Generated. */
    private static final long serialVersionUID = 1142007602702569328L;

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
        super("Replay analysis failed.");
    }

    /**
     * Constructor.
     * 
     * @param cause
     *            cause
     */
    public ReplayAnalysisException(Throwable cause) {
        super(getMessage(cause), cause);
    }

    /**
     * Constructor.
     * 
     * @param message
     *            message
     */
    public ReplayAnalysisException(String message) {
        super(message);
    }
}
