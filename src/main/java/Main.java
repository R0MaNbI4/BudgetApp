import budget.domain.Account;
import budget.domain.Category;
import budget.domain.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Transaction transaction = new Transaction(1, 200, new Account(1, "Test", 0), new Category(1,"Food", false), new Date(3508921313L), "3 пачки молока");
        try {
            throw new RuntimeException("This is test error");
        } catch (RuntimeException e) {
            logger.trace("Test message\n" + transaction.toString(), e);
        }
    }
}