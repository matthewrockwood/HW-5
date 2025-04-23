package com.example.logindemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class DBManager {
    final static String db_url = "jdbc:mysql://localhost:3306/csc411db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            File file = new File("csc411db.conf");
            Scanner infile = new Scanner(file, "utf-8");
            String username = infile.next();
            String password = infile.next();
            conn = DriverManager.getConnection(db_url, username, password);
            System.out.println("Connected to DB");
        } catch (ClassNotFoundException | SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void createUserTable() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS user (" +
                    "email VARCHAR(32) NOT NULL PRIMARY KEY," +
                    "salt VARCHAR(128) NOT NULL," +
                    "password VARCHAR(512) NOT NULL)";
            stmt.execute(query);
            System.out.println("The user table is ready");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // This is the method you call from your controller: register user with raw password
    public static void registerUser(String email, String rawPassword) {
        byte[] salt = PasswordManager.generateSalt();
        String saltStr = PasswordManager.byteArrayToString(salt);
        String hashedPassword = PasswordManager.generatePasswordHash(rawPassword, salt);

        User newUser = new User(email, saltStr, hashedPassword);
        addUser(newUser);
        System.out.println("User registered: " + email);
    }

    public static void addUser(User newUser) {
        String query = "INSERT INTO user (email, salt, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUser.getEmail());
            stmt.setString(2, newUser.getSalt());
            stmt.setString(3, newUser.getPassword());
            int row = stmt.executeUpdate();
            System.out.println(row + " row added to the user table");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String retrievePassword(String email) {
        String query = "SELECT password FROM user WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("password");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String retrieveSalt(String email) {
        String query = "SELECT salt FROM user WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("salt");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
