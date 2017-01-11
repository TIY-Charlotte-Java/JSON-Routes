package com.theironyard.charlotte;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by graceconnelly on 1/11/17.
 */
public class User {
    Integer id;
    String username;
    String address;
    String email;

    public User() {}

    public User(Integer id, String username, String address, String email) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.email = email;
    }

    public User(String username, String address, String email) {
        this.username = username;
        this.address = address;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    //populates a restaurant based on selectMethods.
    public static User populateUser(ResultSet results) throws SQLException {
        Integer id = results.getInt("id");
        String username = results.getString("username");
        String address = results.getString("address");
        String email = results.getString("email");
        return new User(id, username, address, email);
    }
    //START Methods that get things from the database
    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM users");
        while (results.next()) {
            users.add(populateUser(results));
        }
        return users;
    }
    //START Methods that modify the database.
    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }
    public static void insertUser(Connection conn, User newU) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, newU.getUsername());
        stmt.setString(2, newU.getAddress());
        stmt.setString(3, newU.getEmail());
        stmt.execute();
    }
    public static void updateUser(Connection conn, User  updateU) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ?, address = ?, email = ? WHERE ID = ?");
        stmt.setString(1, updateU.getUsername());
        stmt.setString(2,updateU.getAddress());
        stmt.setString(3,updateU.getEmail());
        stmt.setInt(4,updateU.getId());
        stmt.execute();
    }
    public static void deleteUser(Connection conn, int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE ID = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }

}