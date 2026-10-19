
package com.campusfix.ui;

import com.campusfix.config.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComplaintHistoryScreen extends JFrame {

    private final JTextField complaintIdField = new JTextField(12);

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{
                            "Old Status",
                            "New Status",
                            "Changed By",
                            "Changed At",
                            "Notes"
                    }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public ComplaintHistoryScreen() {
        setTitle("CampusFix - Complaint History");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Complaint ID (e.g. CF-12):"));
        searchPanel.add(complaintIdField);

        JButton searchButton = new JButton("Search History");
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        JTable historyTable = new JTable(tableModel);
        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> loadHistory());
        closeButton.addActionListener(e -> dispose());
    }

    private void loadHistory() {
        String code = complaintIdField.getText().trim();

        if (!code.matches("(?i)CF-\\d+")) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid Complaint ID, e.g. CF-12.");
            return;
        }

        int complaintId = Integer.parseInt(code.substring(3));
        tableModel.setRowCount(0);

        String sql = """
                SELECT h.old_status, h.new_status, h.changed_by,
                       h.changed_at, h.notes
                FROM complaint_history h
                JOIN complaints c ON h.complaint_id = c.complaint_id
                WHERE c.complaint_id = ?
                ORDER BY h.changed_at ASC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("old_status") == null
                                    ? "—" : rs.getString("old_status"),
                            rs.getString("new_status"),
                            rs.getInt("changed_by"),
                            rs.getTimestamp("changed_at"),
                            rs.getString("notes")
                    });
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No history found for this complaint."
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this, "Unable to load history: " + e.getMessage());
        }
    }
}
