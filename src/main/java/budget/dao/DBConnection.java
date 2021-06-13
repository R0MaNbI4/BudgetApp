package budget.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/budget", "root", "chapa135_");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}