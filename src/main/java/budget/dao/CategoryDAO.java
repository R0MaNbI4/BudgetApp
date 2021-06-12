package budget.dao;

import budget.domain.Account;
import budget.domain.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO {
    public static void addCategory(Category category) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO category (`name`, `isIncome`) VALUES ('?', '?');");
            statement.setString(1, category.getName());
            statement.setBoolean(2, category.isIncome());
            statement.execute();
        } catch (SQLException e) {
            throw new DAOException("Can't add category", e);
        }
    }

    public static void updateCategory(int id, Category category) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE category SET name = '?' isIncome = '?' WHERE id = '?'");
            statement.setString(1, category.getName());
            statement.setBoolean(2, category.isIncome());
            statement.setInt(3, id);
        } catch (SQLException e) {
            throw new DAOException(String.format("Can't update category with id = %d", id), e);
        }
    }

    public static void deleteCategory(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM category WHERE id = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new DAOException(String.format("Can't delete category with id = %d", id), e);
        }
    }

    public static Category getCategoryById(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            return new Category(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getBoolean(3)
            );
        } catch (SQLException e) {
            throw new DAOException(String.format("Category with id = %d not found", id), e);
        }
    }
}
