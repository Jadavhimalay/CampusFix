
package com.campusfix.dao;

import com.campusfix.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackDAO {

    public boolean submitFeedback(
            int complaintId,
            int studentId,
            int rating,
            String comments
    ) throws SQLException {

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5.");
        }

        String sql = """
                INSERT INTO feedback (complaint_id, rating, comments)
                SELECT complaint_id, ?, ?
                FROM complaints
                WHERE complaint_id = ?
                  AND student_id = ?
                  AND status IN ('RESOLVED', 'CLOSED')
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, rating);
            statement.setString(2, comments);
            statement.setInt(3, complaintId);
            statement.setInt(4, studentId);

            return statement.executeUpdate() == 1;
        }
    }
}
