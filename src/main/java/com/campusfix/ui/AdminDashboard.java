
package com.campusfix.ui;

import com.campusfix.model.User;
import com.campusfix.ui.AssignComplaintScreen;
import com.campusfix.model.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private final User admin;

    public AdminDashboard(User admin) {
        this.admin = admin;

        setTitle("CampusFix - Admin Dashboard");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(240, 245, 252));

        JLabel heading = new JLabel(
                "Welcome, " + admin.getName() + "!",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(new Color(30, 58, 95));

        JLabel subtitle = new JLabel(
                "Admin Dashboard - CampusFix",
                SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.setOpaque(false);
        header.add(heading);
        header.add(subtitle);

        JButton allComplaintsButton =
                new JButton("View All Complaints");
        JButton assignButton =
                new JButton("Assign Complaint to Staff");
        JButton manageStaffButton =
                new JButton("Manage Staff");
        JButton reportsButton =
                new JButton("Reports & History");
        JButton historyButton =
                new JButton("Complaint History");
        JButton logoutButton =
                new JButton("Logout");

        JPanel buttons = new JPanel(new GridLayout(3, 2, 15, 15));
        buttons.setOpaque(false);
        buttons.add(allComplaintsButton);
        buttons.add(assignButton);
        buttons.add(manageStaffButton);
        buttons.add(reportsButton);
        buttons.add(historyButton);
        buttons.add(logoutButton);


        allComplaintsButton.addActionListener(e ->
                new AllComplaintsScreen(admin).setVisible(true));

        assignButton.addActionListener(e -> {
            if (admin instanceof Admin) {
                new AssignComplaintScreen((Admin) admin).setVisible(true);
            }
        });

        manageStaffButton.addActionListener(e -> {
            if (admin instanceof Admin) {
                new ManageStaffScreen((Admin) admin).setVisible(true);
            }
        });

        reportsButton.addActionListener(e -> {
            new ComplaintHistoryScreen().setVisible(true);
        });

        historyButton.addActionListener(e -> {
            new ComplaintHistoryScreen().setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new WelcomeScreen().setVisible(true);
        });

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(buttons, BorderLayout.CENTER);

        add(mainPanel);
    }
}
