package BackEnd;

import BackEnd.Data.Models.User;
import WindowManagers.Dashboard;
import WindowManagers.Login;
import WindowManagers.Registration;

import javax.swing.*;

import static BackEnd.Data.DB.DatabaseInitializer.initDatabase;

public class App {
    private JFrame currentFrame;

    public static void main(String[] args) {
        initDatabase();

        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.showLoginWindow();
        });
    }

    public void showLoginWindow() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        currentFrame = new Login(this);
        currentFrame.setVisible(true);
    }

    public void showRegistrationWindow() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        currentFrame = new Registration(this);
        currentFrame.setVisible(true);
    }

    public void showDashboardWindow(User user) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        currentFrame = new Dashboard(this, user);
        currentFrame.setVisible(true);
    }
}
