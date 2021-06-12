package budget.domain;

import budget.dao.AccountDAO;
import budget.dao.CategoryDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Account {
    private static final Logger logger = LogManager.getLogger(Account.class);

    private int id;
    private String name;
    private int balance;

    public Account(int id, String name, int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }

    void transferTo (Account account, int amount) {
        this.balance -= amount;
        account.balance += amount;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public static void addAccount(Account account) {
        AccountDAO.addAccount(account);
    }

    public static void updateAccount(int id, Account account) {
        AccountDAO.updateAccount(id, account);
    }

    public static void updateAccount(Account original, Account modified) {
        int id = original.getId();
        if (id != -1) {
            AccountDAO.updateAccount(id, modified);
        } else {
            logger.debug("Account hasn't id\n" + original.toString());
        }
    }

    public static void deleteAccount(int id) {
        AccountDAO.deleteAccount(id);
    }

    public static void deleteAccount(Account account) {
        int id = account.getId();
        if (id != -1) {
            AccountDAO.deleteAccount(id);
        } else {
            logger.debug("Account hasn't id\n" + account.toString());
        }
    }

    public static Account getAccountById(int id) {
        return AccountDAO.getAccountById(id);
    }
}
