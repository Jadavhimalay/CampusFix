
package com.campusfix.ui;

import com.campusfix.service.PasswordUtil;
import com.campusfix.dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class StudentRegistrationScreen extends JFrame {

    private final JTextField nameField = new JTextField(20);
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);

    private final UserDAO userDAO = new UserDAO();

    public StudentRegistrationScreen() {
        setTitle("CampusFix - Student Registration");
        setSize(460, 370);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Student Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(30, 58, 95));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridwidth = 1;
        addField(form, gbc, 1, "Full Name:", nameField);
        addField(form, gbc, 2, "Username:", usernameField);
        addField(form, gbc, 3, "Password:", passwordField);
        addField(form, gbc, 4, "Confirm Password:", confirmPasswordField);

        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);
        buttons.add(registerButton);
        buttons.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        registerButton.addActionListener(e -> registerStudent());
        cancelButton.addActionListener(e -> dispose());

        add(form);
    }

    private void addField(JPanel panel, GridBagConstraints gbc,
                          int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void registerStudent() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();

        char[] passwordChars = passwordField.getPassword();
        char[] confirmChars = confirmPasswordField.getPassword();

        String password = new String(passwordChars);
        String confirmPassword = new String(confirmChars);

        try {
            if (name.isEmpty() || username.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            if (password.length() < 8) {
                JOptionPane.showMessageDialog(
                        this, "Password must be at least 8 characters.");
                return;
            }

            if (userDAO.usernameExists(username)) {
                JOptionPane.showMessageDialog(
                        this, "Username already exists. Choose another.");
                return;
            }

            String passwordHash = PasswordUtil.hashPassword(password);

            boolean saved = userDAO.registerStudent(
                    name, username, passwordHash);

            if (saved) {
                JOptionPane.showMessageDialog(
                        this, "Registration successful! You can now log in.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this, "Registration could not be completed.");
            }

        } catch (SQLException | GeneralSecurityException ex) {
            JOptionPane.showMessageDialog(
                    this, "Registration failed. Please check database setup.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            java.util.Arrays.fill(passwordChars, '\0');
            java.util.Arrays.fill(confirmChars, '\0');
            passwordField.setText("");
            confirmPasswordField.setText("");
        }
    }
}
