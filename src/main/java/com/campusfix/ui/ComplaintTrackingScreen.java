
package com.campusfix.ui;

import com.campusfix.dao.ComplaintDAO;
import com.campusfix.dao.ComplaintHistoryDAO;
import com.campusfix.model.Complaint;
import com.campusfix.model.ComplaintHistory;
import com.campusfix.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComplaintTrackingScreen extends JFrame {

    private final User student;
    private final JTextField complaintIdField = new JTextField(12);
    private final JLabel statusLabel = new JLabel("Current Status: —");
    private final DefaultTableModel historyModel =
            new DefaultTableModel(
                    new String[]{"Old Status", "New Status", "Changed By",
                            "Date & Time", "Notes"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public ComplaintTrackingScreen(User student) {
        this.student = student;

        setTitle("CampusFix - Track Complaint");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel heading = new JLabel("Track Your Complaint",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        JButton trackButton = new JButton("Track");
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Complaint ID:"));
        searchPanel.add(complaintIdField);
        searchPanel.add(trackButton);

        JPanel topPanel = new JPanel(new BorderLayout(5, 10));
        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        JTable historyTable = new JTable(historyModel);
        historyTable.setRowHeight(25);

        JButton closeButton = new JButton("Close");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeButton);

        trackButton.addActionListener(e -> loadTracking());
        closeButton.addActionListener(e -> dispose());

        main.add(topPanel, BorderLayout.NORTH);
        main.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.SOUTH);

        add(main);
    }

    private void loadTracking() {
        String input = complaintIdField.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a complaint ID, for example CF-12.");
            return;
        }

        String numericId = input.toUpperCase().startsWith("CF-")
                ? input.substring(3).trim() : input;

        final int complaintId;
        try {
            complaintId = Integer.parseInt(numericId);
            if (complaintId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid ID, for example CF-12.");
            return;
        }

        try {
            ComplaintDAO complaintDAO = new ComplaintDAO();

            List<Complaint> complaints =
                    complaintDAO.getComplaintsByStudent(
                            student.getUserId());

            Complaint found = null;
            for (Complaint complaint : complaints) {
                if (complaint.getComplaintId() == complaintId) {
                    found = complaint;
                    break;
                }
            }

            historyModel.setRowCount(0);

            if (found == null) {
                statusLabel.setText("Current Status: —");
                JOptionPane.showMessageDialog(this,
                        "Complaint not found for your account.");
                return;
            }

            statusLabel.setText("Current Status: "
                    + found.getStatus());

            ComplaintHistoryDAO historyDAO =
                    new ComplaintHistoryDAO();

            List<ComplaintHistory> historyList =
                    historyDAO.getHistoryByComplaintId(complaintId);

            for (ComplaintHistory history : historyList) {
                historyModel.addRow(new Object[]{
                        history.getOldStatus() == null
                                ? "—" : history.getOldStatus(),
                        history.getNewStatus(),
                        history.getChangedBy(),
                        history.getChangedAt(),
                        history.getNotes()
                });
            }

            if (historyList.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Current status loaded. No history records yet.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load tracking information:\n"
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
