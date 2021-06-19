package budget.dao;

import budget.domain.Account;
import budget.domain.Category;
import budget.domain.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

public class TransactionDAO {
    private static final Logger logger = LogManager.getLogger(TransactionDAO.class);
    public static final int valueMaxIntDigits = 9;
    public static final int valueMaxFractionDigits = 2;

    public static void addTransaction(Transaction transaction) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO transaction (`value`, `account_id`, `category_id`, `date`, `note`) VALUES (?, ?, ?, ?, ?);");
            statement.setInt(1, transaction.getValue());
            statement.setInt(2, transaction.getAccount().getId());
            statement.setInt(3, transaction.getCategory().getId());
            statement.setDate(4, new java.sql.Date(transaction.getDate().getTime()));
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
            PreparedStatement statement = connection.prepareStatement("UPDATE transaction SET value = ? account_id = ? category_id = ? date = ? note = ? WHERE id = ?");
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
                return new ArrayList<Transaction>();
            }

            return getTransactions(resultSet);
        } catch (SQLException e) {
            logger.error(String.format("Something went wrong while trying to get transactions in the range from %s to %s", startDate, endDate), e);
            throw new DAOException("SWW", e);
        }
    }

    public static Date getDateOfFirstTransaction() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT date FROM transaction ORDER BY date ASC LIMIT 1");
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                logger.warn("First transaction not found");
                return new Date();
            }

            Date date = resultSet.getDate(1);
            logger.trace("Found first transaction with date " + date);
            return date;
        } catch (SQLException e) {
            logger.error("Can't found first transaction\n", e);
            throw new DAOException("Can't found first transaction", e);
        }
    }

    public static Date getDateOfLastTransaction() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT date FROM transaction ORDER BY date DESC LIMIT 1");
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                logger.warn("Last transaction not found");
                return new Date();
            }

            Date date = resultSet.getDate(1);
            logger.trace("Found last transaction with date " + date);
            return date;
        } catch (SQLException e) {
            logger.error("Can't found last transaction\n", e);
            throw new DAOException("Can't found last transaction", e);
        }
    }

    public static ArrayList<Transaction> getTransactionsByPeriodAndCategory(Date startDate, Date endDate, Category category) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction WHERE date BETWEEN ? AND ? AND category_id = ?;");
            statement.setDate(1, new java.sql.Date(startDate.getTime()));
            statement.setDate(2, new java.sql.Date(endDate.getTime()));
            statement.setInt(3, category.getId());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return new ArrayList<Transaction>();
            }

            return getTransactions(resultSet);
        } catch (SQLException e) {
            logger.error("Can't found last transaction\n", e);
            throw new DAOException("Can't found last transaction", e);
        }
    }

    public static ArrayList<Transaction> getTransactionsByPeriodAndCategoryAndAccount(Date startDate, Date endDate, Category category, Account account) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM transaction WHERE date BETWEEN ? AND ? AND category_id = ? AND account_id = ?;");
            statement.setDate(1, new java.sql.Date(startDate.getTime()));
            statement.setDate(2, new java.sql.Date(endDate.getTime()));
            statement.setInt(3, category.getId());
            statement.setInt(4, account.getId());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return new ArrayList<Transaction>();
            }

            return getTransactions(resultSet);
        } catch (SQLException e) {
            logger.error("Can't found last transaction\n", e);
            throw new DAOException("Can't found last transaction", e);
        }
    }

    private static ArrayList<Transaction> getTransactions(ResultSet resultSet) throws SQLException {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        do {
            result.add(
                new Transaction(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    AccountDAO.getAccountById(resultSet.getInt(3)),
                    CategoryDAO.getCategoryById(resultSet.getInt(4)),
                    resultSet.getDate(5),
                    resultSet.getString(6)
                ));
        } while(resultSet.next());
        return result;
    }
}
