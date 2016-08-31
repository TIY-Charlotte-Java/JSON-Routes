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

    public static void createTables(Connection conn) throws SQLException { // create users and songs table.  will join through u.id = s.userId
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username NVARCHAR, address NVARCHAR, email NVARCHAR)");
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                ((request, response) -> {
                    ArrayList<User> usersList = DataRepository.selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(usersList);
                })
        );

        Spark.post(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User userObj = p.parse(body, User.class);
                    DataRepository.insertUser(conn, userObj.username, userObj.address, userObj.email);
                    return "";
                })
        );

        Spark.put(
                "/user/:id",
                ((request, response) -> {
                    int userId = Integer.valueOf(request.params(":id"));
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User userObj = p.parse(body, User.class);
                    DataRepository.updateUser(conn, userObj.username, userObj.address, userObj.email, userId);
                    return "";
                })
        );

        Spark.delete(
                "/user/:id",
                ((request, response) -> {
                    int userId = Integer.valueOf(request.params(":id"));

                    DataRepository.deleteUser(conn, userId);
                    return "";
                })

        );


    }
}
