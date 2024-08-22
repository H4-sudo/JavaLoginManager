package WindowManagers;

import BackEnd.App;
import BackEnd.Data.DAO.UserDAO;
import BackEnd.Data.Models.User;
import BackEnd.Security.PasswordHashed;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Login extends JFrame {
    private JButton loginButton;
    private JButton registerButton;
    private JPanel mainPanel;
    private JLabel usernameLabel;
    private JTextField usernameInput;
    private JPasswordField userPassword;
    private JLabel usernameChecker;
    private JLabel passwordLabel;

    public Login(App app) {
        setContentPane(mainPanel);
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameLabel.setText("Username:");
        passwordLabel.setText("Password:");
        registerButton.setText("Register");
        loginButton.setText("Login");

        usernameInput.getDocument().addDocumentListener(new UsernameInputListener());

        loginButton.addActionListener(_ -> {
            String username = usernameInput.getText();
            String password = new String(userPassword.getPassword());

            try {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserByUsername(username);

                if (user != null) {
                    boolean isPasswordValid = PasswordHashed.validatePassword(password, user.getPassword(),
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
                JOptionPane.showMessageDialog(Login.this, "An error occurred while trying " +
                        "to log in. Please try again.");
            }
        });

        registerButton.addActionListener(_ -> app.showRegistrationWindow());
    }

    private boolean checkUsersRegistered() {
        String username = usernameInput.getText();
        List<String> usersOnSystem;

        try {
            UserDAO userDAO = new UserDAO();
            usersOnSystem = userDAO.getUsernamesFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (String usernames : usersOnSystem) {
            return usernames.matches(username);
        }

        return false;
    }

    private class UsernameInputListener implements DocumentListener {
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
            if (checkUsersRegistered()) {
                usernameChecker.setText("Username found.");
                usernameChecker.setForeground(Color.GREEN);
            } else {
                usernameChecker.setText("No user in DB found for " + username);
                usernameChecker.setForeground(Color.RED);
            }
        }
    }
}
