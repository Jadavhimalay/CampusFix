package com.campusfix.model;

public class Admin extends User {

    public Admin(int userId, String name, String username) {
        super(userId, name, username, "ADMIN");
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome to Admin Dashboard!");
        System.out.println("Admin Name: " + getName());
        System.out.println("Available features:");
        System.out.println("1. View All Complaints");
        System.out.println("2. Assign Complaints to Staff");
        System.out.println("3. Manage Staff");
        System.out.println("4. View Reports and History");
    }
}

