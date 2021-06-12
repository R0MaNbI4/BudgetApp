package budget.domain;

import budget.dao.CategoryDAO;
import budget.dao.TransactionDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Category {
    private static final Logger logger = LogManager.getLogger(Category.class);

    private int id;
    private String name;
    private boolean isIncome;

    public Category(int id, String name, boolean isIncome) {
        this.id = id;
        this.name = name;
        this.isIncome = isIncome;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isIncome=" + isIncome +
                '}';
    }

    public Category(String name, boolean isIncome) {
        this(-1, name, isIncome);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public static void addCategory(Category category) {
        CategoryDAO.addCategory(category);
    }

    public static void updateCategory(int id, Category category) {
        CategoryDAO.updateCategory(id, category);
    }

    public static void updateCategory(Category original, Category modified) {
        int id = original.getId();
        if (id != -1) {
            CategoryDAO.updateCategory(id, modified);
        } else {
            logger.debug("Category hasn't id\n" + original.toString());
        }
    }

    public static void deleteCategory(int id) {
        CategoryDAO.deleteCategory(id);
    }

    public static void deleteCategory(Category category) {
        int id = category.getId();
        if (id != -1) {
            CategoryDAO.deleteCategory(id);
        } else {
            logger.debug("Category hasn't id\n" + category.toString());
        }
    }

    public static Category getCategoryById(int id) {
        return CategoryDAO.getCategoryById(id);
    }
}
