package WindowManagers;

import BackEnd.App;
import BackEnd.Data.DAO.UserDAO;
import BackEnd.Data.DB.DatabaseConnection;
import BackEnd.Data.Models.User;
import BackEnd.Security.PasswordHashed;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

public class Registration extends JFrame {
    private JPanel mainPanel;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField txtPasswordInitial;
    private JPasswordField txtPasswordFinal;
    private JTextField txtUsername;
    private JButton registerButton;
    private JLabel labelFirstName;
    private JLabel labelLastName;
    private JLabel labelEmail;
    private JLabel labelPassword;
    private JLabel labelPasswordConfirmation;
    private JLabel labelUsername;
    private JLabel labelPasswordCheck;
    private JLabel labelPasswordMatchCheck;
    private JLabel labelUsernameCheck;

    public Registration(App app) {
        setContentPane(mainPanel);
        setTitle("Registration");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        labelPassword.setText("Password:");
        labelPasswordConfirmation.setText("Confirm Password:");

        registerButton.addActionListener(_ -> {
            if (validateFields()) {
                String password = new String(txtPasswordInitial.getPassword());
                String[] hashedPasswordAndSalt = PasswordHashed.hashPassword(password);

                User user = new User();
                user.setFirstName(txtFirstName.getText().trim());
                user.setLastName(txtLastName.getText().trim());
                user.setEmail(txtEmail.getText().trim());
                user.setUsername(txtUsername.getText().trim());
                user.setPassword(hashedPasswordAndSalt[1]);
                user.setSalt(hashedPasswordAndSalt[0]);

                try {
                    UserDAO userDAO = new UserDAO();
                    userDAO.saveUser(user);
                    DatabaseConnection.reloadConnection();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                app.showLoginWindow();
            } else {
                JOptionPane.showMessageDialog(null, "Please correct all the errors",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        txtUsername.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkUsername();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkUsername();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkUsername();
            }

            private void checkUsername() {
                String username = txtUsername.getText().trim();

                if (meetUsernameCriteria(username)) {
                    labelUsernameCheck.setText("Username is allowed.");
                    labelUsernameCheck.setForeground(Color.GREEN);
                } else {
                    labelUsernameCheck.setText("Username invalid, ensure it contains '#'.");
                    labelUsernameCheck.setForeground(Color.RED);
                }
            }
        });

        txtPasswordInitial.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkPassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkPassword();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkPassword();
            }

            private void checkPassword() {
                String password = new String(txtPasswordInitial.getPassword());

                if (meetPasswordComplexity(password)) {
                    labelPasswordCheck.setText("Password meets the correct criteria.");
                    labelPasswordCheck.setForeground(Color.GREEN);
                } else {
                    labelPasswordCheck.setText("Password Criteria: 8 characters or more, Mixed Case, Numbers and special characters");
                    labelPasswordCheck.setForeground(Color.RED);
                }
            }
        });

        txtPasswordFinal.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkMatch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkMatch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkMatch();
            }

            private void checkMatch() {
                String initPassword = new String(txtPasswordInitial.getPassword());
                String finalPassword = new String(txtPasswordFinal.getPassword());

                if (passwordsMatch(initPassword, finalPassword)) {
                    labelPasswordMatchCheck.setText("Passwords match! You may proceed.");
                    labelPasswordMatchCheck.setForeground(Color.GREEN);
                } else {
                    labelPasswordMatchCheck.setText("Passwords don't match, please try again.");
                    labelPasswordMatchCheck.setForeground(Color.RED);
                }
            }
        });
    }

//    Checks if the criteria for the username is met. Made it to be only 8 characters long for data quality.
    public boolean meetUsernameCriteria(String username) {
        username = username.trim();
        return username.startsWith("#") && username.length() == 8;
    }

//    Regex to check if the password meets the minimum requirements.
    public boolean meetPasswordComplexity(String password) {
        return password.length() >= 8 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find() &&
                Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    public boolean passwordsMatch(String initPassword, String finalPassword) {
        return Objects.equals(initPassword, finalPassword);
    }

//    May look redundant, but it works as it should, also helps the user experience.
    private boolean validateFields() {
        boolean allValid = true;

        if (txtFirstName.getText().trim().isEmpty()) {
            labelFirstName.setForeground(Color.RED);
            allValid = false;
        } else {
            labelFirstName.setForeground(Color.BLACK);
        }

        if (txtLastName.getText().trim().isEmpty()) {
            labelLastName.setForeground(Color.RED);
            allValid = false;
        } else {
            labelLastName.setForeground(Color.BLACK);
        }

        if (txtEmail.getText().trim().isEmpty() || !isValidEmail(txtEmail.getText())) {
            labelEmail.setForeground(Color.RED);
            allValid = false;
        } else {
            labelEmail.setForeground(Color.BLACK);
        }

        if (!meetUsernameCriteria(txtUsername.getText().trim())) {
            labelUsername.setForeground(Color.RED);
            allValid = false;
        } else {
            labelUsername.setForeground(Color.BLACK);
        }

        String initPassword = new String(txtPasswordInitial.getPassword());
        String finalPassword = new String(txtPasswordFinal.getPassword());

        if (!meetPasswordComplexity(initPassword)) {
            labelPasswordCheck.setForeground(Color.RED);
            allValid = false;
        } else {
            labelPasswordCheck.setForeground(Color.BLACK);
        }

        if (!passwordsMatch(initPassword, finalPassword)) {
            labelPasswordMatchCheck.setForeground(Color.RED);
            allValid = false;
        } else {
            labelPasswordMatchCheck.setForeground(Color.BLACK);
        }

        return allValid;
    }
    private boolean isValidEmail(String email) {
//        Made use of a stock standard email validation regex.
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }
}
