package WindowManagers;

import BackEnd.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                app.showDashboardWindow();
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
