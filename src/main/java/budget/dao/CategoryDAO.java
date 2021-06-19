package budget.dao;

import budget.domain.Account;
import budget.domain.Category;
import budget.ui.statistics.CategoryType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class CategoryDAO {
    private static final Logger logger = LogManager.getLogger(CategoryDAO.class);

    public static void addCategory(Category category) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO category (`name`, `isIncome`) VALUES (?, ?);");
            statement.setString(1, category.getName());
            statement.setBoolean(2, category.isIncome());
            statement.execute();
            logger.info("category added\n" + category.toString());
        } catch (SQLException e) {
            logger.error("Can't add category\n" + category.toString(), e);
            throw new DAOException("Can't add category", e);
        }
    }

    public static void updateCategory(int id, Category category) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE category SET name = ? isIncome = ? WHERE id = ?");
            statement.setString(1, category.getName());
            statement.setBoolean(2, category.isIncome());
            statement.setInt(3, id);
            statement.execute();
            logger.info("category updated\n" + category.toString());
        } catch (SQLException e) {
            logger.error("Can't update category\n" + category.toString(), e);
            throw new DAOException(String.format("Can't update category with id = %d", id), e);
        }
    }

    public static void deleteCategory(int id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM category WHERE id = ?");
            statement.setInt(1, id);
            statement.execute();
            logger.info(String.format("category deleted with id = %d", id));
        } catch (SQLException e) {
            logger.error(String.format("Can't delete category with id = %d", id), e);
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

            Category category = new Category(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getBoolean(3)
            );

            logger.trace("category found\n" + category.toString());
            return category;
        } catch (SQLException e) {
            logger.error(String.format("Can't find category with id = %d", id), e);
            throw new DAOException(String.format("Can't find category with id = %d", id), e);
        }
    }

    public static ArrayList<Category> getAllCategories() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM category");
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            ArrayList<Category> result = new ArrayList<>();
            do {
                result.add(
                        new Category(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getBoolean(3)
                        ));
            } while (resultSet.next());

            logger.trace("categories found\n" + result.toString());
            return result;
        } catch (SQLException e) {
            logger.error("Something went wrong while trying to get all categories", e);
            throw new DAOException("Something went wrong while trying to get all categories", e);
        }
    }

    public static ArrayList<Category> getCategoriesByType(CategoryType categoryType) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE isIncome = ?");

            switch (categoryType) {
                case INCOME:
                    statement.setInt(1, 1);
                    break;
                case EXPENSE:
                    statement.setInt(1, 0);
                    break;
                case INCOME_AND_EXPENSE:
                    connection.close();
                    return getAllCategories();
                case NONE:
                    return new ArrayList<>();
            }

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            ArrayList<Category> result = new ArrayList<>();
            do {
                result.add(
                        new Category(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getBoolean(3)
                        ));
            } while (resultSet.next());

            return result;
        } catch (SQLException e) {
            logger.error("Can't find category with type " + categoryType, e);
            throw new DAOException("Can't find category with type " + categoryType, e);
        }
    }
}
