
package com.campusfix.main;

import com.campusfix.config.DatabaseConnection;
import com.campusfix.service.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CreateTestAccounts {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DatabaseConnection.getConnection()) {

            createAccount(connection, scanner, "ADMIN", false);

            System.out.println();
            createAccount(connection, scanner, "STAFF", true);

            System.out.println("\nAccount setup completed.");

        } catch (Exception e) {
            System.out.println("Account setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createAccount(
            Connection connection,
            Scanner scanner,
            String role,
            boolean needsDepartment) throws Exception {

        System.out.println("=== Create " + role + " Account ===");

        System.out.print("Full name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password (minimum 8 characters): ");
        String password = scanner.nextLine();

        if (name.isEmpty() || username.isEmpty() || password.length() < 8) {
            System.out.println("Invalid details. Name and username are required; password must have at least 8 characters.");
            return;
        }

        if (usernameExists(connection, username)) {
            System.out.println("Username already exists. Choose another username.");
            return;
        }

        Integer departmentId = null;

        if (needsDepartment) {
            System.out.println("Available departments:");

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT department_id, department_name FROM departments ORDER BY department_id");
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    System.out.println(
                            rs.getInt("department_id") + " - " +
                                    rs.getString("department_name"));
                }
            }

            System.out.print("Department ID (enter 0 for none): ");
            int enteredId = Integer.parseInt(scanner.nextLine().trim());

            if (enteredId != 0) {
                try (PreparedStatement ps = connection.prepareStatement(
                        "SELECT department_id FROM departments WHERE department_id = ?")) {
                    ps.setInt(1, enteredId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("Department ID not found. Account not created.");
                            return;
                        }
                    }
                }

                departmentId = enteredId;
            }
        }

        String passwordHash = PasswordUtil.hashPassword(password);

        String sql = """
                INSERT INTO users
                    (name, username, password_hash, role, department_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, passwordHash);
            ps.setString(4, role);

            if (departmentId == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, departmentId);
            }

            ps.executeUpdate();
            System.out.println(role + " account created successfully!");
        }
    }

    private static boolean usernameExists(
            Connection connection, String username) throws SQLException {

        String sql = "SELECT user_id FROM users WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
