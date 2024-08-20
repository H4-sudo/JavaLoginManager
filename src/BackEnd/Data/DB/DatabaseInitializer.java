package BackEnd.Data.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static final String dbURL = "jdbc:sqlite:login_app.db";

    public static void initDatabase() {
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String sqlQuery = "CREATE TABLE IF NOT EXISTS User (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "firstName TEXT NOT NULL," +
                        "lastName TEXT NOT NULL," +
                        "email TEXT NOT NULL UNIQUE," +
                        "username TEXT NOT NULL UNIQUE," +
                        "password TEXT NOT NULL" +
                        ");";

                statement.execute(sqlQuery);
                System.out.println("Table for user created.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
