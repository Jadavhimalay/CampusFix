
package com.campusfix.ui;

import com.campusfix.model.User;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private final User student;

    public StudentDashboard(User student) {
        this.student = student;

        setTitle("CampusFix - Student Dashboard");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(240, 245, 252));

        JLabel heading = new JLabel(
                "Welcome, " + student.getName() + "!",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(new Color(30, 58, 95));

        JLabel subtitle = new JLabel(
                "Student Dashboard - CampusFix",
                SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.setOpaque(false);
        header.add(heading);
        header.add(subtitle);

        JButton registerButton = new JButton("Register Complaint");
        JButton complaintsButton = new JButton("My Complaints");
        JButton feedbackButton = new JButton("Submit Feedback");
        JButton logoutButton = new JButton("Logout");
        JButton trackButton = new JButton("Track Complaint");

        trackButton.addActionListener(e ->
                new ComplaintTrackingScreen(student).setVisible(true));

        registerButton.addActionListener(e ->
                new ComplaintRegistrationScreen(student).setVisible(true));

        complaintsButton.addActionListener(e ->
                new MyComplaintsScreen(student).setVisible(true));

        feedbackButton.addActionListener(e ->
                new FeedbackScreen(student).setVisible(true));

        logoutButton.addActionListener(e -> {
            dispose();
            new WelcomeScreen().setVisible(true);

        });


        JPanel buttons = new JPanel(new GridLayout(2, 3, 15, 15));
        buttons.setOpaque(false);
        buttons.add(registerButton);
        buttons.add(complaintsButton);
        buttons.add(feedbackButton);
        buttons.add(logoutButton);
        buttons.add(trackButton);



        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(buttons, BorderLayout.CENTER);

        add(mainPanel);
    }
}
