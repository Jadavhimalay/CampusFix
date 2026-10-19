package com.campusfix.ui;

import com.campusfix.model.User;
import com.campusfix.model.Admin;
import com.campusfix.service.LoginService;

import javax.swing.*;
import java.awt.*;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Arrays;

public class AdminLoginScreen extends JFrame {

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final LoginService loginService = new LoginService();

    public AdminLoginScreen() {
        setTitle("CampusFix - Admin Login");
        setSize(430, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 245, 252));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(30, 58, 95));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        form.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        form.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        form.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        form.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setBackground(Color.WHITE);
        buttons.add(loginButton);
        buttons.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        loginButton.addActionListener(e -> login());
        backButton.addActionListener(e -> dispose());

        panel.add(form);
        add(panel);
    }

    private void login() {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try {
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Enter both username and password.");
                return;
            }

            User user = loginService.login(username, password);

            if (!(user instanceof Admin)) {
                JOptionPane.showMessageDialog(
                        this, "Invalid admin username or password.");
                return;
            }

            dispose();
            new AdminDashboard(user).setVisible(true);

        } catch (SQLException | GeneralSecurityException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login failed. Please check the database connection.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        } finally {
            Arrays.fill(passwordChars, '\0');
            passwordField.setText("");
        }
    }
}
