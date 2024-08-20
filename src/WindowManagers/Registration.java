package WindowManagers;

import BackEnd.App;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showLoginWindow();
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
                String username = String.valueOf(txtUsername);

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

    public boolean meetUsernameCriteria(String username) {
        return username.contains("#") && username.length() <= 8;
    }

    public boolean meetPasswordComplexity(String password) {
        return password.length() >= 8 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find() &&
                Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    public boolean passwordsMatch(String initPassword, String finalPassword) {
        return Objects.equals(initPassword, finalPassword);
    }
}
