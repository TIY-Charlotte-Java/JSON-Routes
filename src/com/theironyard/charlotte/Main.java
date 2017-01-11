package com.theironyard.charlotte;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void main(String[] args) throws SQLException{
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                ((req, res) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(users);
                })
        );

        Spark.post(
                "/user",
                ((req, res) -> {
                    String body = req.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    insertUser(conn, user.username, user.address, user.email);
                    return "";
                })
        );

        Spark.put(
                "/user",
                ((req, res) -> {
                    String body = req.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    updateUser(conn, user.id, user.username, user.address, user.email);
                    return "";
                })
        );

        Spark.delete(
                "/user/:id",
                ((req, res) -> {
                    int id = Integer.valueOf(req.params(":id"));
                    deleteUser(conn, id);

//                    res.redirect("");

                    return "";
                })
        );
    }

    public static void insertUser (Connection conn, String username, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }
    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id, username, address, email));
        }
        return users;
    }
    public static User updateUser (Connection conn, int id, String username, String address, String email) throws SQLException {
        User current = getUserById(conn, id);
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username=?, address=?, email=? WHERE id=?");
        stmt.setInt(4, id);
        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
        return current;
    }

    public static User getUserById(Connection conn, int id) throws SQLException {
        User user = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id=?");
        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();

        if (result.next()) {
            String username = result.getString("username");
            String address = result.getString("address");
            String email = result.getString("email");
            user = new User(id, username, address, email);
        }
        return user;
    }

    public static void deleteUser (Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id=?");
        stmt.setInt(1, id);
        stmt.execute();
    }
}
