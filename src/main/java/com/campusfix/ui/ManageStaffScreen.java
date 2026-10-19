
package com.campusfix.ui;

import com.campusfix.dao.StaffDAO;
import com.campusfix.model.Admin;
import com.campusfix.model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ManageStaffScreen extends JFrame {

    private final JTable staffTable;
    private final DefaultTableModel tableModel;

    public ManageStaffScreen(Admin admin) {
        setTitle("CampusFix - Manage Staff");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("Staff Members", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"Staff ID", "Name", "Username", "Department ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable = new JTable(tableModel);
        add(new JScrollPane(staffTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadStaff());
        closeButton.addActionListener(e -> dispose());

        loadStaff();
    }

    private void loadStaff() {
        tableModel.setRowCount(0);

        try {
            List<Staff> staffList = new StaffDAO().getAllStaff();

            for (Staff staff : staffList) {
                tableModel.addRow(new Object[]{
                        staff.getUserId(),
                        staff.getName(),
                        staff.getUsername(),
                        staff.getDepartmentId() == 0
                                ? "Not assigned"
                                : staff.getDepartmentId()
                });
            }

            if (staffList.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No staff members found in the database."
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load staff: " + e.getMessage()
            );
        }
    }
}
