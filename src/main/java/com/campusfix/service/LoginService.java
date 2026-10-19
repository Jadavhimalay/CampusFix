
package com.campusfix.service;

import com.campusfix.config.DatabaseConnection;
import com.campusfix.model.User;
import com.campusfix.model.Student;
import com.campusfix.model.Staff;
import com.campusfix.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.GeneralSecurityException;

public class LoginService {

    public User login(String username, String password)
            throws SQLException, GeneralSecurityException {

        String sql = """
                SELECT user_id, name, username, password_hash, role,
                       department_id
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String storedHash = rs.getString("password_hash");

                if (!PasswordUtil.verifyPassword(password, storedHash)) {
                    return null;
                }

                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String userName = rs.getString("username");
                String role = rs.getString("role");
                int departmentId = rs.getInt("department_id");

                return switch (role) {
                    case "STUDENT" ->
                            new Student(userId, name, userName);
                    case "STAFF" ->
                            new Staff(userId, name, userName, departmentId);
                    case "ADMIN" ->
                            new Admin(userId, name, userName);
                    default -> null;
                };
            }
        }
    }
}
