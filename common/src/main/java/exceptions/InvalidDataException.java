package exceptions;

public class InvalidDataException extends Exception {
    public InvalidDataException() {
    }
    public InvalidDataException(String message) {
        super(message);
    }
    public InvalidDataException(String message, Exception e) {
        super(message, e);
    }
}