
package com.campusfix.ui;

import com.campusfix.dao.ComplaintDAO;
import com.campusfix.model.Complaint;
import com.campusfix.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AssignedComplaintsScreen extends JFrame {

    private final User staff;
    private final DefaultTableModel tableModel;

    public AssignedComplaintsScreen(User staff) {
        this.staff = staff;

        setTitle("CampusFix - Assigned Complaints");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel heading = new JLabel(
                "My Assigned Complaints",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        String[] columns = {
                "Complaint ID", "Category", "Location",
                "Priority", "Status", "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);

        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        refreshButton.addActionListener(e -> loadComplaints());
        closeButton.addActionListener(e -> dispose());

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadComplaints();
    }

    private void loadComplaints() {
        tableModel.setRowCount(0);

        try {
            ComplaintDAO dao = new ComplaintDAO();

            List<Complaint> complaints =
                    dao.getAssignedComplaints(staff.getUserId());

            for (Complaint complaint : complaints) {
                String code = complaint.getComplaintCode();

                if (code == null || code.isBlank()) {
                    code = "CF-" + complaint.getComplaintId();
                }

                tableModel.addRow(new Object[]{
                        code,
                        complaint.getCategory(),
                        complaint.getLocation(),
                        complaint.getPriority(),
                        complaint.getStatus(),
                        complaint.getDescription()
                });
            }

            if (complaints.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No complaints have been assigned to you yet.");
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
