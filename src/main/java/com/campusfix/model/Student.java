package com.campusfix.model;

public class Student extends User {

    public Student(int userId, String name, String username) {
        super(userId, name, username, "STUDENT");
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome to Student Dashboard!");
        System.out.println("Student Name: " + getName());
        System.out.println("Available features:");
        System.out.println("1. Register Complaint");
        System.out.println("2. View My Complaints");
        System.out.println("3. Track Complaint Status");
        System.out.println("4. Submit Feedback");
    }
}

