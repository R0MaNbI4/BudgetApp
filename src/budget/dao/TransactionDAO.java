package budget.dao;

import budget.domain.Category;
import budget.domain.Transaction;

import java.sql.*;
import java.util.ArrayList;

public class TransactionDAO {
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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            throw new DAOException(String.format("Can't update transaction with id = %d", id), e);
        }
    }

    public static void deleteTransaction(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM transaction WHERE id = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
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

            return new Transaction(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    AccountDAO.getAccountById(resultSet.getInt(3)),
                    CategoryDAO.getCategoryById(resultSet.getInt(4)),
                    resultSet.getDate(5),
                    resultSet.getString(6)
            );
        } catch (SQLException e) {
            throw new DAOException(String.format("Can't get transaction with id = %d", id), e);
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

            return result;
        } catch (SQLException e) {
            throw new DAOException("SWW", e);
        }
    }
}
