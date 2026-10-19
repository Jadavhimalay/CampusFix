
package com.campusfix.model;

import java.time.LocalDateTime;

public class Feedback {

    private int feedbackId;
    private int complaintId;
    private int rating;
    private String comments;
    private LocalDateTime submittedAt;

    public Feedback(int complaintId, int rating, String comments) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5."
            );
        }

        this.complaintId = complaintId;
        this.rating = rating;
        this.comments = comments;
        this.submittedAt = LocalDateTime.now();
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5."
            );
        }
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
}
