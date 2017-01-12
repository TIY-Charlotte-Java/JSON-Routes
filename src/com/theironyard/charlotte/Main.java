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

    public static void insertUser(Connection conn, User u) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, u.getUsername());
        stmt.setString(2, u.getAddress());
        stmt.setString(3, u.getEmail());
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM users");
        while (results.next()) {
            Integer id = results.getInt("id");
            String username = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id, username, address, email));
        }
        return users;
    }

    public static void deleteUsers(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void updateUser(Connection conn, User u) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ?, address = ?, email = ? WHERE id = ?");
        stmt.setString(1, u.getUsername());
        stmt.setString(2, u.getAddress());
        stmt.setString(3, u.getEmail());
        stmt.setInt(4, u.getId());
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");

        //Create a GET route called /user that calls selectUsers and returns the data as JSON.
        Spark.get(
                "/user",
                (request, response) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(users);
                });

        //Create a POST route called /user that parses request.body() into a User object
        //and calls insertUser to put it in the database.
        Spark.post(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    insertUser(conn, new User(user.username, user.address, user.email));
                    return "";
                }
        );
        //Create a PUT route called /user that parses request.body() into a User object
        //and calls updateUser to update it in the database.
        Spark.put(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    updateUser(conn, new User(user.id, user.username, user.address, user.email));
                    return "";
                }
        );
        //Create a DELETE route called /user/:id that gets the id via request.params(":id")
        //and gives it to deleteUser to delete it in the database.
        Spark.delete(
                "/user/:id",
                (request, response) -> {
                    //Integer id = Integer.valueOf(request.params("id"));
                    deleteUsers(conn, Integer.valueOf((request.params(":id"))));
                    //response.redirect("/user"); WHY DON'T I NEED THIS - WASTED SOOO MUCH TIME!
                    return "";
                }
        );
        Spark.init();
    }
}