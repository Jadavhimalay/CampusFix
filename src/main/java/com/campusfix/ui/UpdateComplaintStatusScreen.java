
package com.campusfix.ui;

import com.campusfix.dao.ComplaintDAO;
import com.campusfix.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UpdateComplaintStatusScreen extends JFrame {

    private final User staff;
    private final JTextField complaintIdField = new JTextField(15);
    private final JComboBox<String> statusBox =
            new JComboBox<>(new String[]{
                    "ASSIGNED", "IN_PROGRESS", "RESOLVED"
            });
    private final JTextArea notesArea = new JTextArea(5, 25);

    public UpdateComplaintStatusScreen(User staff) {
        this.staff = staff;

        setTitle("CampusFix - Update Complaint Status");
        setSize(550, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel heading = new JLabel(
                "Update Complaint Status",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 23));

        JPanel form = new JPanel(new GridLayout(2, 2, 12, 15));
        form.add(new JLabel("Complaint ID (e.g. CF-12):"));
        form.add(complaintIdField);
        form.add(new JLabel("New Status:"));
        form.add(statusBox);

        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        JPanel notesPanel = new JPanel(new BorderLayout(5, 5));
        notesPanel.add(new JLabel("Progress / Resolution Notes:"),
                BorderLayout.NORTH);
        notesPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        JButton updateButton = new JButton("Update Status");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(form, BorderLayout.NORTH);
        centerPanel.add(notesPanel, BorderLayout.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        updateButton.addActionListener(e -> updateStatus());
        cancelButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void updateStatus() {
        String input = complaintIdField.getText().trim();

        if (input.toUpperCase().startsWith("CF-")) {
            input = input.substring(3).trim();
        }

        final int complaintId;

        try {
            complaintId = Integer.parseInt(input);
            if (complaintId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid ID, for example CF-12.");
            return;
        }

        String newStatus = (String) statusBox.getSelectedItem();
        String notes = notesArea.getText().trim();

        if (notes.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter progress or resolution notes.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Update complaint CF-" + complaintId
                        + " to " + newStatus + "?",
                "Confirm Status Update",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ComplaintDAO dao = new ComplaintDAO();

            boolean updated = dao.updateAssignedStatusWithHistory(
                    complaintId,
                    staff.getUserId(),
                    newStatus,
                    notes
            );

            if (updated) {
                JOptionPane.showMessageDialog(
                        this, "Complaint status and history updated!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this, "Complaint ID not found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Update failed:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
