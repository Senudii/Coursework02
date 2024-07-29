package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.imageio.ImageIO;

public class DashboardUI extends JFrame {

    private Connection connection;

    public DashboardUI() {
        setTitle("Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "admin", "senudi");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initUI();
    }

    private void initUI() {
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new GridBagLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        buttonPanel.setOpaque(false); // Make the panel transparent

        JButton manageBooksButton = createButton("Books");
        JButton manageCategoriesButton = createButton("Categories");
        JButton manageMembersButton = createButton("Members");
        JButton manageBorrowingsButton = createButton("Borrowings");
        JButton manageFinesButton = createButton("Fines");

        buttonPanel.add(manageBooksButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        buttonPanel.add(manageCategoriesButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(manageMembersButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(manageBorrowingsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(manageFinesButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 1;
        gbc.weighty = 0.9;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(400, 50)); // Set button size
        button.setMaximumSize(new Dimension(400, 50)); // Ensure the button does not grow larger than this size
        button.setFont(new Font("Arial", Font.BOLD, 20)); // Set button font size
        button.setBackground(new Color(209, 190, 168)); // Set button background color
        button.setForeground(Color.BLACK); // Set button text color
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            // Action listeners should be specific to each button
            if (text.equals("Books")) {
                new ManagebooksUI(connection).setVisible(true);
                this.dispose(); // Close the DashboardUI
            } else if (text.equals("Categories")) {
                new ManagecategoriesUI(connection).setVisible(true);
                this.dispose(); // Close the DashboardUI
            } else if (text.equals("Members")) {
                new ManagemembersUI(connection).setVisible(true);
                this.dispose(); // Close the DashboardUI
            } else if (text.equals("Borrowings")) {
                new ManageborrowingsUI(connection).setVisible(true);
                this.dispose(); // Close the DashboardUI
            } else if (text.equals("Fines")) {
                new ManagefinesUI(connection).setVisible(true);
                this.dispose(); // Close the DashboardUI
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardUI ex = new DashboardUI();
            ex.setVisible(true);
        });
    }

    private static class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(getClass().getResource("/assets/Library.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
