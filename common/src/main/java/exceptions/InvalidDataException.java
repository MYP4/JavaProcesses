package exceptions;

/**
 * Exception thrown when invalid data is encountered during processing.
 */
public class InvalidDataException extends Exception {

    /**
     * Constructs a new InvalidDataException with null as its detail message.
     */
    public InvalidDataException() {
        super();
    }

    /**
     * Constructs a new InvalidDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidDataException(final String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidDataException with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param e the cause of the exception
     */
    public InvalidDataException(final String message, final Exception e) {
        super(message, e);
    }
}
