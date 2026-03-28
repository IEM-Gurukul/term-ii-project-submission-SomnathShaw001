package exceptions;

public class DeadlineConflictException extends Exception {
    public DeadlineConflictException(String message) {
        super(message);
    }
}
