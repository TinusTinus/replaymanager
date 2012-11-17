package nl.tinus.umvc3replayanalyser.ocr;

/**
 * Indicates that something has gone wrong while attempting optical character recognition.
 * 
 * @author Martijn van de Rijdt
 */
public class OCRException extends Exception {

    /** Generated. */
    private static final long serialVersionUID = -3433722707352162943L;

    /** Constructor. */
    public OCRException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param message message
     */
    public OCRException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param cause cause
     */
    public OCRException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * 
     * @param message message
     * @param cause cause
     */
    public OCRException(String message, Throwable cause) {
        super(message, cause);
    }
}
