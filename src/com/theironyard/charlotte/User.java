package com.theironyard.charlotte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by mfahrner on 8/31/16.
 */
public class User {
    Integer id;
    String username;
    String address;
    String email;

    public User() {

    }

    public User(Integer id, String username, String address, String email) {
        this.id = id;
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

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1, name);

        ResultSet results = stmt.executeQuery();

        if (results.next()) {
            Integer id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");

            return new User(id, username, address, email);
        }
        return null;
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> userList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");

        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            Integer id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");

            userList.add(new User(id, username, address, email));
        }
        return userList;
    }

    public static void insertUser(Connection conn, String username, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");

        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);

        stmt.execute();
    }

    public static void deleteUser(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");

        stmt.setInt(1, id);

        stmt.execute();
    }

    public static void updateUser(Connection conn, String username, String address, String email, Integer id)
            throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ?, address = ?, email = ?" +
                "WHERE id = ?");

        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.setInt(4, id);

        stmt.execute();
    }
}
