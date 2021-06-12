package budget.dao;

import budget.domain.Category;
import budget.domain.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

public class TransactionDAO {
    private static final Logger logger = LogManager.getLogger(TransactionDAO.class);

    public static void addTransaction(Transaction transaction) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO transaction (`value`, `account_id`, `category_id`, `date`, `note`) VALUES ('?', '?', '?', '?', '?');");
            statement.setInt(1, transaction.getValue());
            statement.setInt(2, transaction.getAccount().getId());
            statement.setInt(3, transaction.getCategory().getId());
            statement.setDate(4, (java.sql.Date) transaction.getDate());
            statement.setString(5, transaction.getNote());
            statement.execute();
            logger.info("transaction added\n" + transaction.toString());
        } catch (SQLException e) {
            logger.error("Can't add transaction\n" + transaction.toString(), e);
            throw new DAOException("Can't add transaction", e);
        }
    }

    public static void updateTransaction(int id, Transaction transaction) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE transaction SET value = '?' account_id = '?' category_id = '?' date = '?' note = '?' WHERE id = '?'");
            statement.setInt(1, transaction.getValue());
            statement.setInt(2, transaction.getAccount().getId());
            statement.setInt(3, transaction.getCategory().getId());
            statement.setDate(4, (java.sql.Date) transaction.getDate());
            statement.setString(5, transaction.getNote());
            statement.setInt(6, id);
            statement.execute();
            logger.info("transaction updated\n" + transaction.toString());
        } catch (SQLException e) {
            logger.error("Can't update transaction\n" + transaction.toString(), e);
            throw new DAOException(String.format("Can't update transaction with id = %d", id), e);
        }
    }

    public static void deleteTransaction(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM transaction WHERE id = ?");
            statement.setInt(1, id);
            statement.execute();
            logger.info(String.format("transaction deleted with id = %d"));
        } catch (SQLException e) {
            logger.error(String.format("Can't delete transaction with id = %d", id), e);
            throw new DAOException(String.format("Can't delete transaction with id = %d", id), e);
        }
    }

    public static Transaction getTransactionById(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            Transaction transaction = new Transaction(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    AccountDAO.getAccountById(resultSet.getInt(3)),
                    CategoryDAO.getCategoryById(resultSet.getInt(4)),
                    resultSet.getDate(5),
                    resultSet.getString(6)
            );

            logger.trace("transaction found\n" + transaction.toString());
            return transaction;
        } catch (SQLException e) {
            logger.error(String.format("Can't found transaction with id = %d", id), e);
            throw new DAOException(String.format("Can't found transaction with id = %d", id), e);
        }
    }

    public static ArrayList<Transaction> getTransactionByDate(Date startDate, Date endDate) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction WHERE (date > ?) AND (date < ?)");
            statement.setDate(1, (java.sql.Date) startDate);
            statement.setDate(2, (java.sql.Date) endDate);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            ArrayList<Transaction> result = new ArrayList<>();
            while(resultSet.next()) {
                result.add(
                    new Transaction(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        AccountDAO.getAccountById(resultSet.getInt(3)),
                        CategoryDAO.getCategoryById(resultSet.getInt(4)),
                        resultSet.getDate(5),
                        resultSet.getString(6)
                ));
            }

            logger.trace("transactions found\n" + result.toString());
            return result;
        } catch (SQLException e) {
            logger.error(String.format("Can't found transaction where the date is in the range from %s to %s", startDate, endDate), e);
            throw new DAOException("SWW", e);
        }
    }
}
