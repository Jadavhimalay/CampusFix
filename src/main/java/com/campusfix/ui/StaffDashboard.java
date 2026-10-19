
package com.campusfix.ui;

import com.campusfix.model.User;

import javax.swing.*;
import java.awt.*;

public class StaffDashboard extends JFrame {

    private final User staff;

    public StaffDashboard(User staff) {
        this.staff = staff;

        setTitle("CampusFix - Staff Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(240, 245, 252));

        JLabel heading = new JLabel(
                "Welcome, " + staff.getName() + "!",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(new Color(30, 58, 95));

        JLabel subtitle = new JLabel(
                "Staff Dashboard - CampusFix",
                SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.setOpaque(false);
        header.add(heading);
        header.add(subtitle);

        JButton assignedButton = new JButton("Assigned Complaints");
        JButton updateButton = new JButton("Update Complaint Status");
        JButton logoutButton = new JButton("Logout");

        JPanel buttons = new JPanel(new GridLayout(3, 1, 15, 15));
        buttons.setOpaque(false);
        buttons.add(assignedButton);
        buttons.add(updateButton);
        buttons.add(logoutButton);

        assignedButton.addActionListener(e ->
                new AssignedComplaintsScreen(staff).setVisible(true));

        updateButton.addActionListener(e ->
                new UpdateComplaintStatusScreen(staff).setVisible(true));

        logoutButton.addActionListener(e -> {
            dispose();
            new WelcomeScreen().setVisible(true);
        });

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(buttons, BorderLayout.CENTER);

        add(mainPanel);
    }
}
