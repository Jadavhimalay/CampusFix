package com.campusfix.dao;

import com.campusfix.config.DatabaseConnection;
import com.campusfix.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean registerStudent(
            String name,
            String username,
            String passwordHash) throws SQLException {

        String sql = """
                INSERT INTO users (name, username, password_hash, role)
                VALUES (?, ?, ?, 'STUDENT')
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, passwordHash);

            return statement.executeUpdate() == 1;
        }
    }
}
