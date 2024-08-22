package BackEnd.Data.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:login_app.db";

    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static synchronized void reloadConnection() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        } else if (connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection(URL);
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e);
        }
    }
}
