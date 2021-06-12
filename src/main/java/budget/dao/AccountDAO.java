package budget.dao;

import budget.domain.Account;
import budget.domain.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    public static void addAccount(Account account) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO account (`name`, `balance`) VALUES ('?', '?');");
            statement.setString(1, account.getName());
            statement.setInt(2, account.getBalance());
            statement.execute();
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            throw new DAOException(String.format("Can't update account with id = %d", id), e);
        }
    }

    public static void deleteAccount(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM account WHERE id = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
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

            return new Account(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getInt(3)
            );
        } catch (SQLException e) {
            throw new DAOException(String.format("Account with id = %d not found", id), e);
        }
    }
}
