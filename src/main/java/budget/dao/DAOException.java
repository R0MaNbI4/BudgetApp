package budget.dao;

public class DAOException extends RuntimeException {
    DAOException(String message) {
        super(message);
    }

    DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
