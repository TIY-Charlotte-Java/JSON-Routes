package com.theironyard.charlotte;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    // creates the Table in the database
    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }

    //this shows what populates the table so far
    public static ArrayList<User> selectUsers(Connection conn)  throws SQLException{
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String userName = results.getString("username");
            String address = results.getString("address");
            String email = results.getString("email");
            users.add(new User(id, userName, address, email));
        }
        return users;
    }
    // this inserts a new user to the table
    public static void insertUser(Connection conn, User r) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?, ?)");
        stmt.setString(1, r.getUsername());
        stmt.setString(2,r.getAddress());
        stmt.setString(3,r.getEmail());
        stmt.execute();
    }
    //this allows you manipulate a previously stored user
    public static void updateUser(Connection conn, User r) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET username = ? ,address = ?, email = ? WHERE ID = ?");
        stmt.setString(1, r.getUsername());
        stmt.setString(2, r.getAddress());
        stmt.setString(3, r.getEmail());
        stmt.setInt(4, r.getId());
        stmt.execute();
    }
    //this allows you to delete a user that is stored in the table
    public static void deleteUser(Connection conn, int id) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        //this pathway shows whats populating the user table
        Spark.get(
                "/user",
                (request, response) -> {
                    ArrayList<User> users = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(users);
                }
        );
        // this pathway allows you to use the method add a new users to the table
        Spark.post(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User usr = p.parse(body, User.class);
                    insertUser(conn, usr);
                    return "";
                }
        );
        // this pathway allows you to use the method to update a previously inserted user
        Spark.put(
                "/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User usr = p.parse(body, User.class);
                    updateUser(conn, usr);
                    return "";
                }
        );
        // this pathway allows you use the method to delete a previously inserted user
        Spark.delete(
                "/user/:id",
                (request, response) -> {
//                    String body = request.body();
//                    JsonParser p = new JsonParser();
//                    User usr = p.parse(body, User.class);

                    deleteUser(conn, Integer.parseInt(request.params("id")));
                    return"";
                }
        );
    }
}
