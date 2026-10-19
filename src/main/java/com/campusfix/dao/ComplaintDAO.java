package com.campusfix.dao;

import com.campusfix.config.DatabaseConnection;
import com.campusfix.model.Complaint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComplaintDAO {

    public String saveComplaint(Complaint complaint)
            throws SQLException {

        String insertSql = """
               
                INSERT INTO complaints
               (student_id, category, location, description,
                priority, status, department_id)
               VALUES (?, ?, ?, ?, ?, 'PENDING', ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int complaintId;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     insertSql,
                                     PreparedStatement.RETURN_GENERATED_KEYS)) {

                    statement.setInt(1, complaint.getStudentId());
                    statement.setString(2, complaint.getCategory());
                    statement.setString(3, complaint.getLocation());
                    statement.setString(4, complaint.getDescription());
                    statement.setString(5, complaint.getPriority());
                    statement.setInt(6, complaint.getDepartmentId());

                    statement.executeUpdate();

                    try (ResultSet keys =
                                 statement.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException(
                                    "Could not generate complaint ID.");
                        }

                        complaintId = keys.getInt(1);
                    }
                }

                String complaintCode = "CF-" + complaintId;

                String updateSql = """
                        UPDATE complaints
                        SET complaint_code = ?
                        WHERE complaint_id = ?
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(updateSql)) {

                    statement.setString(1, complaintCode);
                    statement.setInt(2, complaintId);
                    statement.executeUpdate();
                }

                connection.commit();

                complaint.setComplaintId(complaintId);
                complaint.setComplaintCode(complaintCode);

                return complaintCode;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);


            }
        }
    }

    public java.util.List<Complaint> getComplaintsByStudent(int studentId)
            throws java.sql.SQLException {

        java.util.List<Complaint> complaints = new java.util.ArrayList<>();

        String sql = """
            SELECT complaint_id, complaint_code, student_id,
                   category, location, description, priority, status
            FROM complaints
            WHERE student_id = ?
            ORDER BY created_at DESC
            """;

        try (java.sql.Connection connection =
                     com.campusfix.config.DatabaseConnection.getConnection();
             java.sql.PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (java.sql.ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Complaint complaint = new Complaint(
                            rs.getInt("student_id"),
                            rs.getString("category"),
                            rs.getString("location"),
                            rs.getString("description"),
                            rs.getString("priority")
                    );

                    complaint.setComplaintId(rs.getInt("complaint_id"));
                    complaint.setComplaintCode(rs.getString("complaint_code"));
                    complaint.setStatus(rs.getString("status"));

                    complaints.add(complaint);
                }
            }
        }

        return complaints;
    }

    public boolean updateComplaintStatus(
            int complaintId,
            String newStatus
    ) throws SQLException {

        String sql = """
            UPDATE complaints
            SET status = ?,
                resolved_at = CASE
                    WHEN ? = 'RESOLVED'
                         THEN COALESCE(resolved_at, CURRENT_TIMESTAMP)
                    ELSE resolved_at
                END
            WHERE complaint_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newStatus);
            statement.setString(2, newStatus);
            statement.setInt(3, complaintId);

            return statement.executeUpdate() == 1;
        }
    }

    public boolean updateStatusWithHistory(
            int complaintId,
            String newStatus,
            int changedBy,
            String notes
    ) throws SQLException {

        String selectSql =
                "SELECT status FROM complaints WHERE complaint_id = ?";

        String updateSql = """
            UPDATE complaints
            SET status = ?,
                resolved_at = CASE
                    WHEN ? = 'RESOLVED'
                        THEN COALESCE(resolved_at, CURRENT_TIMESTAMP)
                    ELSE resolved_at
                END
            WHERE complaint_id = ?
            """;

        String historySql = """
            INSERT INTO complaint_history
            (complaint_id, old_status, new_status, changed_by, notes)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                String oldStatus;

                try (PreparedStatement statement =
                             connection.prepareStatement(selectSql)) {
                    statement.setInt(1, complaintId);

                    try (ResultSet rs = statement.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }

                        oldStatus = rs.getString("status");
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(updateSql)) {
                    statement.setString(1, newStatus);
                    statement.setString(2, newStatus);
                    statement.setInt(3, complaintId);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(historySql)) {
                    statement.setInt(1, complaintId);
                    statement.setString(2, oldStatus);
                    statement.setString(3, newStatus);
                    statement.setInt(4, changedBy);
                    statement.setString(5, notes);

                    statement.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public java.util.List<Complaint> getAssignedComplaints(int staffId)
            throws SQLException {

        java.util.List<Complaint> complaints = new java.util.ArrayList<>();

        String sql = """
            SELECT complaint_id, complaint_code, student_id,
                   category, location, description, priority, status
            FROM complaints
            WHERE assigned_staff_id = ?
            ORDER BY created_at DESC
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, staffId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Complaint complaint = new Complaint(
                            rs.getInt("student_id"),
                            rs.getString("category"),
                            rs.getString("location"),
                            rs.getString("description"),
                            rs.getString("priority")
                    );

                    complaint.setComplaintId(rs.getInt("complaint_id"));
                    complaint.setComplaintCode(rs.getString("complaint_code"));
                    complaint.setStatus(rs.getString("status"));

                    complaints.add(complaint);
                }
            }
        }

        return complaints;
    }

    public boolean updateAssignedStatusWithHistory(
            int complaintId,
            int staffId,
            String newStatus,
            String notes
    ) throws SQLException {

        String selectSql = """
            SELECT status
            FROM complaints
            WHERE complaint_id = ?
              AND assigned_staff_id = ?
            FOR UPDATE
            """;

        String updateSql = """
            UPDATE complaints
            SET status = ?,
                resolved_at = CASE
                    WHEN ? = 'RESOLVED'
                        THEN COALESCE(resolved_at, CURRENT_TIMESTAMP)
                    ELSE resolved_at
                END
            WHERE complaint_id = ?
              AND assigned_staff_id = ?
            """;

        String historySql = """
            INSERT INTO complaint_history
            (complaint_id, old_status, new_status, changed_by, notes)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                String oldStatus;

                try (PreparedStatement statement =
                             connection.prepareStatement(selectSql)) {
                    statement.setInt(1, complaintId);
                    statement.setInt(2, staffId);

                    try (ResultSet rs = statement.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }
                        oldStatus = rs.getString("status");
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(updateSql)) {
                    statement.setString(1, newStatus);
                    statement.setString(2, newStatus);
                    statement.setInt(3, complaintId);
                    statement.setInt(4, staffId);

                    if (statement.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(historySql)) {
                    statement.setInt(1, complaintId);
                    statement.setString(2, oldStatus);
                    statement.setString(3, newStatus);
                    statement.setInt(4, staffId);
                    statement.setString(5, notes);
                    statement.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public java.util.List<Complaint> getAllComplaints()
            throws SQLException {

        java.util.List<Complaint> complaints = new java.util.ArrayList<>();

        String sql = """
            SELECT complaint_id, complaint_code, student_id,
                   category, location, description, priority, status,
                   assigned_staff_id
            FROM complaints
            ORDER BY created_at DESC
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Complaint complaint = new Complaint(
                        rs.getInt("student_id"),
                        rs.getString("category"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getString("priority")
                );

                complaint.setComplaintId(rs.getInt("complaint_id"));
                complaint.setComplaintCode(rs.getString("complaint_code"));
                complaint.setStatus(rs.getString("status"));

                complaints.add(complaint);
            }
        }

        return complaints;
    }

    public boolean assignComplaintToStaff(
            int complaintId,
            int staffId
    ) throws SQLException {

        String sql = """
            UPDATE complaints
            SET assigned_staff_id = ?,
                status = 'ASSIGNED'
            WHERE complaint_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, staffId);
            statement.setInt(2, complaintId);

            return statement.executeUpdate() == 1;
        }
    }

}
