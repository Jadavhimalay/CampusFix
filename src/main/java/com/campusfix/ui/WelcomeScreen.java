package com.campusfix.ui;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("CampusFix - Smart Campus Complaint System");
        setSize(650, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 245, 252));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("CampusFix");
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setForeground(new Color(30, 58, 95));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Smart Campus Complaint & Maintenance System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel chooseLabel = new JLabel("Select your role to continue");
        chooseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton studentButton = createButton("Student Login");
        JButton staffButton = createButton("Staff Login");
        JButton adminButton = createButton("Admin Login");


        studentButton.addActionListener(e -> {
            new StudentLoginScreen().setVisible(true);
        });

        staffButton.addActionListener(e -> {
            new StaffLoginScreen().setVisible(true);
        });

        adminButton.addActionListener(e -> {
            new AdminLoginScreen().setVisible(true);
        });

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(30));
        content.add(chooseLabel);
        content.add(Box.createVerticalStrut(18));
        content.add(studentButton);
        content.add(Box.createVerticalStrut(10));
        content.add(staffButton);
        content.add(Box.createVerticalStrut(10));
        content.add(adminButton);

        mainPanel.add(content);
        add(mainPanel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 38));
        button.setPreferredSize(new Dimension(250, 38));
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeScreen().setVisible(true);
        });
    }
}
