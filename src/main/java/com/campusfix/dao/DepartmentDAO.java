package com.campusfix.dao;

import com.campusfix.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // Fetch all department names from the database
    public List<String> getAllDepartmentNames() throws SQLException {

        List<String> departments = new ArrayList<>();

        String sql = """
                SELECT department_name
                FROM departments
                ORDER BY department_name
                """;

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                departments.add(rs.getString("department_name"));
            }
        }

        return departments;
    }

    // Fetch categories available for the selected department
    public List<String> getCategoriesByDepartment(
            String departmentName
    ) throws SQLException {

        List<String> categories = new ArrayList<>();

        String sql = """
                SELECT dc.category
                FROM department_categories dc
                JOIN departments d
                    ON d.department_id = dc.department_id
                WHERE d.department_name = ?
                ORDER BY dc.category
                """;

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, departmentName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(rs.getString("category"));
                }
            }
        }

        return categories;
    }
    // Fetch department ID using department name
    public int getDepartmentIdByName(String name)
            throws SQLException {

        String sql =
                "SELECT department_id FROM departments WHERE department_name = ?";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("department_id");
                }
            }
        }

        throw new SQLException("Department not found: " + name);
    }
}
