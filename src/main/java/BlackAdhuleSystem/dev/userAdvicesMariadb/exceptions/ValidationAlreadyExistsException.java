package BlackAdhuleSystem.dev.userAdvicesMariadb.exceptions;

public class ValidationAlreadyExistsException extends RuntimeException {
    public ValidationAlreadyExistsException(String message) {
        super(message);
    }
}