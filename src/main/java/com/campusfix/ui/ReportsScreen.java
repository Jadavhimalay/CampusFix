
package com.campusfix.ui;

import com.campusfix.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsScreen extends JFrame {

    private final JLabel totalLabel = new JLabel("Total Complaints: 0");
    private final JLabel pendingLabel = new JLabel("Pending: 0");
    private final JLabel assignedLabel = new JLabel("Assigned: 0");
    private final JLabel progressLabel = new JLabel("In Progress: 0");
    private final JLabel resolvedLabel = new JLabel("Resolved: 0");
    private final JLabel closedLabel = new JLabel("Closed: 0");

    public ReportsScreen() {
        setTitle("CampusFix - Reports & History");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel heading = new JLabel("Complaint Reports", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        panel.add(heading);
        panel.add(totalLabel);
        panel.add(pendingLabel);
        panel.add(assignedLabel);
        panel.add(progressLabel);
        panel.add(resolvedLabel);
        panel.add(closedLabel);

        JButton refreshButton = new JButton("Refresh Report");
        refreshButton.addActionListener(e -> loadReport());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(refreshButton);
        buttons.add(closeButton);

        add(panel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        loadReport();
    }

    private void loadReport() {
        String sql = """
                SELECT status, COUNT(*) AS total
                FROM complaints
                GROUP BY status
                """;

        int pending = 0;
        int assigned = 0;
        int inProgress = 0;
        int resolved = 0;
        int closed = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("total");

                switch (status) {
                    case "PENDING" -> pending = count;
                    case "ASSIGNED" -> assigned = count;
                    case "IN_PROGRESS" -> inProgress = count;
                    case "RESOLVED" -> resolved = count;
                    case "CLOSED" -> closed = count;
                }
            }

            int total = pending + assigned + inProgress + resolved + closed;

            totalLabel.setText("Total Complaints: " + total);
            pendingLabel.setText("Pending: " + pending);
            assignedLabel.setText("Assigned: " + assigned);
            progressLabel.setText("In Progress: " + inProgress);
            resolvedLabel.setText("Resolved: " + resolved);
            closedLabel.setText("Closed: " + closed);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this, "Unable to load report: " + e.getMessage());
        }
    }
}
