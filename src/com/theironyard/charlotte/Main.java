package com.theironyard.charlotte;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                ((request, response) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(users);
                })
        );

        Spark.post(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    insertUser(conn,new User(user.username, user.address, user.email));
                    return "";
                })
        );

        Spark.put(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    updateUser(conn,new User(user.id, user.username, user.address, user.email));
                    return "";
                })
        );

        Spark.delete(
                "/user/:id",
                ((request, response) -> {
                    int id = Integer.valueOf(request.params("id"));
                    deleteUser(conn, id);
                    return "";
                })
        );
    }

    public static User createUserArray(ResultSet results) throws SQLException {
        Integer id = results.getInt("id");
        String username = results.getString("username");
        String address = results.getString("address");
        String email = results.getString("email");
        return new User(id, username, address, email);
    }
    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM users");
        while (results.next()) {
            users.add(createUserArray(results));
        }
        return users;
    }
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
