import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            throw new RuntimeException("This is test error");
        } catch (RuntimeException e) {
            logger.debug("Test message", e);
        }
    }
}
