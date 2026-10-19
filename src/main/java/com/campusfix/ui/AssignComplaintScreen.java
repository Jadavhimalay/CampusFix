
package com.campusfix.ui;

import com.campusfix.dao.ComplaintDAO;
import com.campusfix.dao.StaffDAO;
import com.campusfix.model.Admin;
import com.campusfix.model.Staff;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AssignComplaintScreen extends JFrame {

    private final Admin admin;
    private final JTextField complaintIdField;
    private final JComboBox<Staff> staffComboBox;

    public AssignComplaintScreen(Admin admin) {
        this.admin = admin;

        setTitle("CampusFix - Assign Complaint");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        complaintIdField = new JTextField(15);
        staffComboBox = new JComboBox<>();

        // Staff dropdown mein name aur username dikhane ke liye
        staffComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value instanceof Staff staff) {
                    setText(staff.getName() + " (" + staff.getUsername() + ")");
                }

                return this;
            }
        });

        JButton assignButton = new JButton("Assign Complaint");
        JButton backButton = new JButton("Back");

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        formPanel.add(new JLabel("Complaint ID (e.g. CF-12):"));
        formPanel.add(complaintIdField);
        formPanel.add(new JLabel("Select Staff:"));
        formPanel.add(staffComboBox);
        formPanel.add(assignButton);
        formPanel.add(backButton);

        add(formPanel);

        loadStaff();

        assignButton.addActionListener(e -> assignComplaint());
        backButton.addActionListener(e -> dispose());
    }

    private void loadStaff() {
        try {
            List<Staff> staffList = new StaffDAO().getAllStaff();

            for (Staff staff : staffList) {
                staffComboBox.addItem(staff);
            }

            if (staffList.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No staff accounts found. Please add a staff account first."
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this, "Could not load staff: " + e.getMessage());
        }
    }

    private void assignComplaint() {
        String complaintCode = complaintIdField.getText().trim();
        Staff selectedStaff = (Staff) staffComboBox.getSelectedItem();

        if (!complaintCode.matches("(?i)CF-\\d+")) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid Complaint ID, e.g. CF-12.");
            return;
        }

        if (selectedStaff == null) {
            JOptionPane.showMessageDialog(
                    this, "Please select a staff member.");
            return;
        }

        int complaintId = Integer.parseInt(
                complaintCode.substring(3));

        try {
            boolean assigned = new ComplaintDAO().assignComplaintToStaff(
                    complaintId, selectedStaff.getUserId());

            if (assigned) {
                JOptionPane.showMessageDialog(
                        this,
                        "Complaint assigned successfully to "
                                + selectedStaff.getName() + "!");
                complaintIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Complaint not found. Check the Complaint ID.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this, "Assignment failed: " + e.getMessage());
        }
    }
}
