package com.theironyard.charlotte;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        User.createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        //    Create a GET route called /user that calls selectUsers and returns the data as JSON.
        Spark.get(
                "/user",
                ((request, response) -> {
                    ArrayList<User> users = User.selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(users);
                })
        );
        //    Create a POST route called /user that parses request.body() into a User object and calls insertUser to put it in the database.
        Spark.post(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    User.insertUser(conn,new User(user.username, user.address, user.email));
                    return "";
                })
        );
        //    Create a PUT route called /user that parses request.body() into a User object and calls updateUser to update it in the database.
        Spark.put(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    User.updateUser(conn,new User(user.id, user.username, user.address, user.email));
                    return "";
                })
        );


//    Create a DELETE route called /user/:id that gets the id via request.params(":id") and gives it to deleteUser to delete it in the database.
    }
}
