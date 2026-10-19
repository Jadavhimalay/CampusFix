package com.campusfix.model;

public class Staff extends User {

    private int departmentId;

    public Staff(int userId, String name, String username, int departmentId) {
        super(userId, name, username, "STAFF");
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome to Staff Dashboard!");
        System.out.println("Staff Name: " + getName());
        System.out.println("Department ID: " + departmentId);
        System.out.println("Available features:");
        System.out.println("1. View Assigned Complaints");
        System.out.println("2. Update Complaint Status");
        System.out.println("3. Add Resolution Notes");
    }
}

