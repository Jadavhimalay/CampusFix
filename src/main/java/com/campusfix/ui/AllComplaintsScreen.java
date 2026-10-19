
package com.campusfix.ui;

import com.campusfix.dao.ComplaintDAO;
import com.campusfix.model.Complaint;
import com.campusfix.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AllComplaintsScreen extends JFrame {

    private final DefaultTableModel model;

    public AllComplaintsScreen(User admin) {
        setTitle("CampusFix - All Complaints");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {
                "Complaint ID", "Student ID", "Category",
                "Location", "Priority", "Status", "Description"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);

        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        refreshButton.addActionListener(e -> loadComplaints());
        closeButton.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(refreshButton);
        bottom.add(closeButton);

        add(new JLabel("All Campus Complaints", SwingConstants.CENTER),
                BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadComplaints();
    }

    private void loadComplaints() {
        model.setRowCount(0);

        try {
            List<Complaint> complaints =
                    new ComplaintDAO().getAllComplaints();

            for (Complaint c : complaints) {
                String code = c.getComplaintCode();

                if (code == null || code.isBlank()) {
                    code = "CF-" + c.getComplaintId();
                }

                model.addRow(new Object[]{
                        code,
                        c.getStudentId(),
                        c.getCategory(),
                        c.getLocation(),
                        c.getPriority(),
                        c.getStatus(),
                        c.getDescription()
                });
            }

            if (complaints.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "No complaints have been submitted yet.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load complaints:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
