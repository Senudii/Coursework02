package ui;

import dao.UserDao;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginUI extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDao userDao;

    public LoginUI(Frame owner) {
        super(owner, "Login", true);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "admin", "senudi");
            userDao = new UserDao(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setLayout(new GridLayout(3, 2));
        setSize(400, 150);
        setLocationRelativeTo(owner);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> authenticate());

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Filler
        add(loginButton);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (userDao.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose(); // Dispose of the LoginUI
                ((JFrame) getOwner()).dispose(); // Dispose of the MainUI

                DashboardUI dashboard = new DashboardUI();
                dashboard.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
