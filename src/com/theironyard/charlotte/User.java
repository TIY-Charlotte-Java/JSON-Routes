package com.theironyard.charlotte;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by emileenmarianayagam on 1/11/17.
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


    public static void insertUser(Connection conn, String username, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement ("insert into users values (NULL, ?, ?, ?)");
        stmt.setString(1,username);
        stmt.setString(2,address);
        stmt.setString(3,email);
        stmt.execute();
    }

    public static ArrayList<User> selectUser(Connection conn) throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("Select * from users");
        while (results.next()){
            Integer id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id,username,address,email));
        }
        return users;
    }

    public static void updateUser(Connection conn, User user) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("update users set username = ?, address = ?, email = ? where id =?");
        stmt.setString(1,user.getUsername());
        stmt.setString(2,user.getAddress());
        stmt.setString(3,user.getEmail());
        stmt.setInt(4,user.getId());
        stmt.execute();
    }
//pass in the conn and an object and deletes the user based on the id
    public static void deleteUser(Connection conn, int id ) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("delete from users where id = ?");
        stmt.setInt(1,id);
        stmt.execute();
    }

    public static void creatTable(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users(id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }
}
