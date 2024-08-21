package WindowManagers;

import BackEnd.App;
import BackEnd.Data.DAO.UserDAO;
import BackEnd.Data.DB.DatabaseConnection;
import BackEnd.Data.Models.User;
import BackEnd.Security.PasswordHasher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class Login extends JFrame {
    private JButton loginButton;
    private JButton registerButton;
    private JPanel mainPanel;
    private JLabel usernameLabel;
    private JTextField usernameInput;
    private JPasswordField userPassword;

    public Login(App app) {
        setContentPane(mainPanel);
        setTitle("Login");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
                    JOptionPane.showMessageDialog(Login.this, "An error occurred while trying to log in. Please try again.");
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
}
