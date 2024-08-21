package WindowManagers;

import BackEnd.App;
import BackEnd.Data.DAO.UserDAO;
import BackEnd.Data.DB.DatabaseConnection;
import BackEnd.Data.Models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;

public class Dashboard extends JFrame {
    private JPanel mainPanel;
    private JLabel greeting;
    private JButton logoutButton;
    private JLabel introduction;
    private JTable registeredUsers;

    public Dashboard(App app, User user) {
        setContentPane(mainPanel);
        setTitle("Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        introduction.setText("Welcome to the SYSTEM.");

        greeting.setText("Congratulations " + user.getFirstName() + " " + user.getLastName() + ", You have made " +
                "it to the second year. Wishing you all the best");

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");

        try {
            UserDAO userDAO = new UserDAO(DatabaseConnection.getConnection());
            List<User> users = userDAO.getAllUsernamesOnSystem();

            tableModel.addRow(new Object[]{"First Name", "Last Name"});
            for (User u : users) {
                tableModel.addRow(new Object[]{u.getFirstName(), u.getLastName()});
            }

            registeredUsers.setModel(tableModel);
            registeredUsers.setFillsViewportHeight(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logoutButton.addActionListener(e -> app.showLoginWindow());
    }
}
