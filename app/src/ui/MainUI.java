package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainUI extends JFrame {

    public MainUI() {
        setTitle("Library Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new GridBagLayout());
        JPanel titleBackground = new JPanel();
        titleBackground.setBackground(new Color(0, 0, 0, 150)); 
        titleBackground.setLayout(new BoxLayout(titleBackground, BoxLayout.Y_AXIS));
        titleBackground.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); 

        JLabel titleLabel = new JLabel("Library Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); 
    titleLabel.setForeground(new Color(209, 190, 168));
        titleBackground.add(titleLabel);

        JButton loginButton = new JButton("Login");
loginButton.setPreferredSize(new Dimension(100, 30));
loginButton.setBackground(new Color(222, 184, 135)); // Set background color to Burlywood
loginButton.setOpaque(true);
loginButton.setBorderPainted(false);// (top, left, bottom, right)// Optional: Removes border for a flat look

loginButton.addActionListener(e -> {
    LoginUI loginUI = new LoginUI(this);
    loginUI.setVisible(true);
});

        titleBackground.add(loginButton);
        titleBackground.setAlignmentX(Component.CENTER_ALIGNMENT); 

    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.insets = new Insets(0, 0, 0, 0); 

        panel.add(titleBackground, gbc); 
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI mainUI = new MainUI();
            mainUI.setVisible(true);
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