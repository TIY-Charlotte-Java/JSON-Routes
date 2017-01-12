package com.theironyard.charlotte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jenniferchang on 8/31/16.
 */
public class DataRepository {

    public static void insertUser(Connection conn, String username, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> usersList = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        while (results.next()) { //if you use if and not while, then it will only show 1.
            int id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            User user = new User(id, username, address, email);
            usersList.add(user);
        }
        return usersList;

    }

    public static void updateUser(Connection conn, String username, String address, String email, int userId) throws SQLException {
        PreparedStatement stmt =
                conn.prepareStatement("UPDATE users SET username = ?, address = ?, email = ? WHERE id = ?");
        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.setInt(4, userId);
        stmt.execute();
    }

    public static void deleteUser(Connection conn, int userId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users where id = ?");
        stmt.setInt(1, userId);
        stmt.execute();
    }




}
