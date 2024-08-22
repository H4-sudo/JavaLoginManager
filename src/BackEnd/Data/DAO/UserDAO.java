package BackEnd.Data.DAO;

import BackEnd.Data.DB.DatabaseConnection;
import BackEnd.Data.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// This is the Data Access Object for the user class.
public class UserDAO {
    private final Connection connection;

    public UserDAO() throws SQLException{
        this.connection = DatabaseConnection.getConnection();
    }

//    This is used to send the data that has been checked into the database for storage. DB used: SQLite
    public void saveUser(User user) throws SQLException {
        String query = "INSERT INTO Users (firstName, lastName, email, username, password, salt) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getSalt());
            statement.executeUpdate();
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToUser(resultSet);
                }
            }
        }
        return null;
    }

    public List<User> getAllUsernamesOnSystem() throws SQLException {
        String query = "SELECT firstName, lastName FROM Users;";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                users.add(user);
            }
        }
        return users;
    }

    public List<String> getUsernamesFromDB() throws SQLException {
        String query = "SELECT username FROM Users;";
        List<String> usernames = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }
        }
        return usernames;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("Id"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setEmail(resultSet.getString("email"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setSalt(resultSet.getString("salt"));

        return user;
    }
}
