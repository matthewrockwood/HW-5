package com.example.logindemo;

import com.sun.source.tree.StatementTree;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
            Scanner infile = new Scanner(file,"utf-8");
           String username = infile.next();
            String password = infile.next();
            conn = DriverManager.getConnection(db_url,username,password);
            System.out.println("Connected to DB");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
    public static void createUserTable(){
        Statement stmt = null;
        Connection conn = null;
        try {

        conn = getConnection();
        String query = "CREATE TABLE IF NOT EXISTS  USER (" +
                "email VARCHAR(32) NOT NULL PRIMARY KEY," +
                "salt VARCHAR(64) NOT NULL," +
                "password VARCHAR(512) NOT NULL)";



            stmt = conn.createStatement();

            stmt.execute(query);
            System.out.println("The user table is ready");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(stmt!=null){
                try{
                    stmt.close();
                }
                 catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void addUser(User newUser) {
        String query = "INSERT INTO users (email,salt,password) VALUES (?,?,?)";
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, newUser.getEmail());
            stmt.setString(2,newUser.getSalt());
            stmt.setString(3,newUser.getPassword());
            int row = stmt.executeUpdate();
            System.out.println(row + " row added to the users table");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
