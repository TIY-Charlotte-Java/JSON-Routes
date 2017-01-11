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

    public static ArrayList<User> selectUser(Connection conn) throws SQLException {
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

    public static void insertUser(Connection conn, String username, String address, String email ) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, username);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }

    public static void updateUser (Connection conn, User user ) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("update users set username = ?, address = ?, email = ? where id = ?");
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getAddress());
        stmt.setString(3, user.getEmail());
        stmt.setInt(4, user.getId());
        stmt.execute();
    }

    public static void deleteUser(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException{
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("Public");

        Spark.get("/user", (req, res) -> {
            ArrayList<User> users = selectUser(conn);
            JsonSerializer s = new JsonSerializer();
            return s.serialize(users);
        });

        Spark.post("/user", (req, res) -> {
            String body = req.body();
            JsonParser p = new JsonParser();
            User user = p.parse(body, User.class);
            insertUser(conn, user.username, user.address, user.email);
            return "";
        });

        Spark.put("/user", (req, res) -> {
            String body = req.body();
            JsonParser p = new JsonParser();
            User user = p.parse(body, User.class);
            updateUser(conn, user);
            return "";
        });

        Spark.delete("/user/:id", (req, res) -> {
            Integer id = Integer.valueOf(req.params("id"));
            deleteUser(conn, id);
            return "";
        });
        Spark.init();
    }
}
