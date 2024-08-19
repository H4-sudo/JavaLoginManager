package WindowManagers;

import BackEnd.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private final App app;
    private JPanel mainPanel;
    private JLabel greeting;
    private JButton logoutButton;

    public Dashboard(App app) {
        this.app = app;
        setContentPane(mainPanel);
        setTitle("Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.showLoginWindow();
            }
        });
    }
}
