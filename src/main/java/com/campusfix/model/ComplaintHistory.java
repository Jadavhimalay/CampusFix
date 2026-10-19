package com.campusfix.model;

import java.time.LocalDateTime;

public class ComplaintHistory {

    private int historyId;
    private int complaintId;
    private String oldStatus;
    private String newStatus;
    private int changedBy;
    private LocalDateTime changedAt;
    private String notes;

    public ComplaintHistory(int complaintId, String oldStatus,
                            String newStatus, int changedBy,
                            String notes) {
        this.complaintId = complaintId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.notes = notes;
        this.changedAt = LocalDateTime.now();
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public int getChangedBy() {
        return changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public String getNotes() {
        return notes;
    }
}

