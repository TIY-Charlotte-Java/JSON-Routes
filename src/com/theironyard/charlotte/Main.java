package com.theironyard.charlotte;

import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, user_name VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static ArrayList<User> selectUsers(Connection conn)  throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String userName = results.getString("user_name");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id, userName, address, email));
        }
        return users;
    }
    public static void insertUser(Connection conn, User r) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?, ?)");
        stmt.setString(1, r.getUserName());
        stmt.setString(2, r.getAddress());
        stmt.setString(3,r.getAddress());
        stmt.setString(4,r.getEmail());
        stmt.execute();
    }
    public static void updateUser(Connection conn, User r) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET user_name = ? ,address = ?, email = ? WHERE ID = ?");
        stmt.setString(1, r.getUserName());
        stmt.setString(2, r.getAddress());
        stmt.setString(3, r.getEmail());
        stmt.setInt(4, r.getId());
        stmt.execute();
    }
    public static void deleteUser(Connection conn, User r) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, r.getId());
        stmt.execute();
    }


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();


    }
}
