package WindowManagers;

import BackEnd.App;
import BackEnd.Data.DAO.UserDAO;
import BackEnd.Data.DB.DatabaseConnection;
import BackEnd.Data.Models.User;
import BackEnd.Security.PasswordHasher;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.LinkedTransferQueue;

public class Login extends JFrame {
    private JButton loginButton;
    private JButton registerButton;
    private JPanel mainPanel;
    private JLabel usernameLabel;
    private JTextField usernameInput;
    private JPasswordField userPassword;
    private JLabel usernameChecker;

    public Login(App app) {
        setContentPane(mainPanel);
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkEnteredUsername();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkEnteredUsername();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkEnteredUsername();
            }

            private void checkEnteredUsername() {
                String username = usernameInput.getText();
                if (checkUsersRegistered(username)) {
                    usernameChecker.setText("Username found.");
                    usernameChecker.setForeground(Color.GREEN);
                } else {
                    usernameChecker.setText("No user in DB found for " + username);
                    usernameChecker.setForeground(Color.RED);
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameInput.getText();
                String password = new String(userPassword.getPassword());

                try (Connection connection = DatabaseConnection.getConnection()) {
                    UserDAO userDAO = new UserDAO(connection);
                    User user = userDAO.getUserByUsername(username);

                    if (user != null) {
                        boolean isPasswordValid = PasswordHasher.validatePassword(password, user.getPassword(),
                                user.getSalt());

                        if (isPasswordValid) {
                            JOptionPane.showMessageDialog(Login.this, "Login Successful!");
                            app.showDashboardWindow(user);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(Login.this, "Username or Password " +
                                    "Incorrect");
                        }
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Username or Password " +
                                "Incorrect");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Login.this, "An error occurred while trying " +
                            "to log in. Please try again.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showRegistrationWindow();
            }
        });
    }

    private boolean checkUsersRegistered(String username) {
        username = usernameInput.getText();
        List<String> usersOnSystem;

        try (Connection connection = DatabaseConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            usersOnSystem = userDAO.getUsernamesFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (String usernames : usersOnSystem) {
            return usernames.matches(username);
        }

        return false;
    }
}
