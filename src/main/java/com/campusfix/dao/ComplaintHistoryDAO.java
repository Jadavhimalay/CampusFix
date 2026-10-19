
package com.campusfix.dao;

import com.campusfix.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ComplaintHistoryDAO {

    public boolean addHistory(
            int complaintId,
            String oldStatus,
            String newStatus,
            int changedBy,
            String notes
    ) throws SQLException {

        String sql = """
                INSERT INTO complaint_history
                (complaint_id, old_status, new_status, changed_by, notes)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);

            if (oldStatus == null) {
                statement.setNull(2, java.sql.Types.VARCHAR);
            } else {
                statement.setString(2, oldStatus);
            }

            statement.setString(3, newStatus);
            statement.setInt(4, changedBy);
            statement.setString(5, notes);

            return statement.executeUpdate() == 1;
        }
    }

    public java.util.List<com.campusfix.model.ComplaintHistory>
    getHistoryByComplaintId(int complaintId)
            throws java.sql.SQLException {

        java.util.List<com.campusfix.model.ComplaintHistory> historyList =
                new java.util.ArrayList<>();

        String sql = """
            SELECT history_id, complaint_id, old_status,
                   new_status, changed_by, changed_at, notes
            FROM complaint_history
            WHERE complaint_id = ?
            ORDER BY changed_at ASC
            """;

        try (java.sql.Connection connection =
                     com.campusfix.config.DatabaseConnection.getConnection();
             java.sql.PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);

            try (java.sql.ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    com.campusfix.model.ComplaintHistory history =
                            new com.campusfix.model.ComplaintHistory(
                                    rs.getInt("complaint_id"),
                                    rs.getString("old_status"),
                                    rs.getString("new_status"),
                                    rs.getInt("changed_by"),
                                    rs.getString("notes")
                            );

                    history.setHistoryId(rs.getInt("history_id"));
                    historyList.add(history);
                }
            }
        }

        return historyList;
    }

}
