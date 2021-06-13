package budget.dao;

import budget.domain.Account;
import budget.domain.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDAO {
    private static final Logger logger = LogManager.getLogger(AccountDAO.class);

    public static void addAccount(Account account) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO account (`name`, `balance`) VALUES ('?', '?');");
            statement.setString(1, account.getName());
            statement.setInt(2, account.getBalance());
            statement.execute();
            logger.info("account added\n" + account.toString());
        } catch (SQLException e) {
            logger.error("Can't add account\n" + account.toString(), e);
            throw new DAOException("Can't add account", e);
        }
    }

    public static void updateAccount(int id, Account account) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE account SET name = '?' balance = '?' WHERE id = '?'");
            statement.setString(1, account.getName());
            statement.setInt(2, account.getBalance());
            statement.setInt(3, id);
            statement.execute();
            logger.info("account updated\n" + account.toString());
        } catch (SQLException e) {
            logger.error("Can't update account\n" + account.toString(), e);
            throw new DAOException(String.format("Can't update account with id = %d", id), e);
        }
    }

    public static void deleteAccount(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM account WHERE id = ?");
            statement.setInt(1, id);
            statement.execute();
            logger.info(String.format("account deleted with id = %d", id));
        } catch (SQLException e) {
            logger.error(String.format("Can't delete account with id = %d", id), e);
            throw new DAOException(String.format("Can't delete account with id = %d", id), e);
        }
    }

    public static Account getAccountById(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM account WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            Account account = new Account(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getInt(3)
            );

            logger.trace("account found\n" + account.toString());
            return account;
        } catch (SQLException e) {
            logger.error(String.format("Can't found account with id = %d", id), e);
            throw new DAOException(String.format("Can't found account with id = %d", id), e);
        }
    }

    public static ArrayList<Account> getAllAccounts() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM account");
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            ArrayList<Account> result = new ArrayList<>();
            do {
                result.add(
                    new Account(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)
                ));
            } while(resultSet.next());

            logger.trace("accounts found\n" + result.toString());
            return result;
        } catch (SQLException e) {
            logger.error("Something went wrong while trying to get all accounts", e);
            throw new DAOException("Something went wrong while trying to get all accounts", e);
        }
    }
}
