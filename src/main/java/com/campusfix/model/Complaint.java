package com.campusfix.model;

import java.time.LocalDateTime;

public class Complaint {

    private int complaintId;
    private String complaintCode;
    private int studentId;
    private int departmentId;
    private String category;
    private String location;
    private String description;
    private String priority;
    private String status;
    private Integer assignedStaffId;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;


    public Complaint(int studentId, String category,
                     String location, String description,
                     String priority) {

        this.studentId = studentId;
        this.category = category;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintCode() {
        return complaintCode;
    }

    public void setComplaintCode(String complaintCode) {
        this.complaintCode = complaintCode;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if ("RESOLVED".equals(status)) {
            this.resolvedAt = LocalDateTime.now();
        }
    }

    public Integer getAssignedStaffId() {
        return assignedStaffId;
    }

    public void setAssignedStaffId(Integer assignedStaffId) {
        this.assignedStaffId = assignedStaffId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}

