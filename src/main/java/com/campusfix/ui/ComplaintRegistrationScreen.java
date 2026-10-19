
package com.campusfix.ui;

import com.campusfix.model.User;
import com.campusfix.dao.ComplaintDAO;
import com.campusfix.model.Complaint;
import java.sql.SQLException;
import com.campusfix.dao.DepartmentDAO;

import javax.swing.*;
import java.awt.*;

public class ComplaintRegistrationScreen extends JFrame {

    private final User student;
    private JComboBox<String> categoryBox;
    private JComboBox<String> priorityBox;
    private JTextField locationField;
    private JTextArea descriptionArea;
    private JComboBox<String> departmentBox;

    public ComplaintRegistrationScreen(User student) {
        this.student = student;

        setTitle("CampusFix - Register Complaint");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );
        panel.setBackground(new Color(240, 245, 252));

        JLabel heading = new JLabel(
                "Register New Complaint",
                SwingConstants.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(25, 45, 85));

        JPanel form = new JPanel(new GridLayout(5, 2, 12, 15));
        form.setOpaque(false);

        categoryBox = new JComboBox<>(new String[]{
                "IT", "ELECTRICAL","PLUMBING","FURNITURE","CLEANING","INFRASTRUCTURE","OTHER"
        });
        departmentBox = new JComboBox<>();

        try {
            DepartmentDAO departmentDAO = new DepartmentDAO();

            for (String name : departmentDAO.getAllDepartmentNames()) {
                departmentBox.addItem(name);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this, "Departments load nahi hue: " + ex.getMessage()
            );
        }


      

        priorityBox = new JComboBox<>(new String[]{
                "LOW", "MEDIUM", "HIGH", "CRITICAL"
        });

        locationField = new JTextField();

        form.add(new JLabel("Complaint Category:"));
        form.add(categoryBox);

        form.add(new JLabel("Department:"));
        form.add(departmentBox);

        form.add(new JLabel("Priority:"));
        form.add(priorityBox);

        form.add(new JLabel("Location:"));
        form.add(locationField);

        form.add(new JLabel("Description:"));
        form.add(new JLabel("Enter details below"));

        descriptionArea = new JTextArea(5, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScroll =
                new JScrollPane(descriptionArea);

        JButton submitButton = new JButton("Submit Complaint");
        JButton cancelButton = new JButton("Cancel");

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        bottomPanel.add(descriptionScroll, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> submitComplaint());

        cancelButton.addActionListener(e -> dispose());

        panel.add(heading, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }


    private void submitComplaint() {
        String category = (String) categoryBox.getSelectedItem();
        String priority = (String) priorityBox.getSelectedItem();
        String location = locationField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (location.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both location and description."
            );
            return;
        }

        try {
            Complaint complaint = new Complaint(
                    student.getUserId(),
                    category,
                    location,
                    description,
                    priority
            );
            String selectedDepartment =
                    (String) departmentBox.getSelectedItem();

            if (selectedDepartment == null) {
                JOptionPane.showMessageDialog(
                        this, "Please select a department.");
                return;
            }

            if (categoryBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(
                        this, "Please select a complaint category.");
                return;
            }

            int departmentId =
                    new DepartmentDAO().getDepartmentIdByName(selectedDepartment);

            complaint.setDepartmentId(departmentId);

            ComplaintDAO dao = new ComplaintDAO();
            String code = dao.saveComplaint(complaint);

            JOptionPane.showMessageDialog(
                    this,
                    "Complaint submitted successfully!\n"
                            + "Complaint ID: " + code + "\n"
                            + "Status: PENDING"
            );

            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + ex.getMessage()
            );
        }
    }
}