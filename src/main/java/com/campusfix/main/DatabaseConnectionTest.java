
package com.campusfix.main;

import com.campusfix.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Database Connected Successfully!");
            System.out.println("Database: " +
                    connection.getCatalog());
        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            System.out.println(e.getMessage());
        }
    }
}
