
package com.campusfix.ui;

import com.campusfix.dao.FeedbackDAO;
import com.campusfix.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class FeedbackScreen extends JFrame {

    private final User student;
    private final JTextField complaintIdField = new JTextField(15);
    private final JComboBox<Integer> ratingBox =
            new JComboBox<>(new Integer[]{5, 4, 3, 2, 1});
    private final JTextArea commentsArea = new JTextArea(5, 25);

    public FeedbackScreen(User student) {
        this.student = student;

        setTitle("CampusFix - Submit Feedback");
        setSize(550, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel heading = new JLabel(
                "Submit Complaint Feedback",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 23));

        JPanel form = new JPanel(new GridLayout(2, 2, 12, 15));
        form.add(new JLabel("Complaint ID (e.g. CF-12):"));
        form.add(complaintIdField);
        form.add(new JLabel("Rating (5 = Excellent):"));
        form.add(ratingBox);

        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);

        JPanel commentsPanel = new JPanel(new BorderLayout(5, 5));
        commentsPanel.add(new JLabel("Comments:"), BorderLayout.NORTH);
        commentsPanel.add(new JScrollPane(commentsArea),
                BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit Feedback");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(submitButton);
        buttons.add(cancelButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(form, BorderLayout.NORTH);
        centerPanel.add(commentsPanel, BorderLayout.CENTER);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> submitFeedback());
        cancelButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void submitFeedback() {
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
                    this,
                    "Enter a valid complaint ID, such as CF-12.");
            return;
        }

        int rating = (Integer) ratingBox.getSelectedItem();
        String comments = commentsArea.getText().trim();

        try {
            FeedbackDAO dao = new FeedbackDAO();

            boolean saved = dao.submitFeedback(
                    complaintId,
                    student.getUserId(),
                    rating,
                    comments
            );

            if (saved) {
                JOptionPane.showMessageDialog(
                        this,
                        "Feedback submitted successfully. Thank you!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Feedback not submitted. Check that the complaint "
                                + "belongs to you and is RESOLVED or CLOSED.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not submit feedback:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
