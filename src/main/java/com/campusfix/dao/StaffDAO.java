
package com.campusfix.dao;

import com.campusfix.config.DatabaseConnection;
import com.campusfix.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    public List<Staff> getAllStaff() throws SQLException {

        List<Staff> staffList = new ArrayList<>();

        String sql = """
                SELECT user_id, name, username, department_id
                FROM users
                WHERE role = 'STAFF'
                ORDER BY name
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int departmentId = rs.getInt("department_id");

                if (rs.wasNull()) {
                    departmentId = 0;
                }

                Staff staff = new Staff(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        departmentId
                );

                staffList.add(staff);
            }
        }

        return staffList;
    }
}
