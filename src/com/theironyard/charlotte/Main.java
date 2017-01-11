package com.theironyard.charlotte;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws SQLException {
	// write your code here
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        User.creatTable(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/user",
                ((request, response) -> {
                    ArrayList<User>user = User.selectUser(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(user);
                })
        );
        Spark.post(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    User.insertUser(conn, user.username,user.address,user.email);
                    return "";
                })
        );

        Spark.put("/user", (
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body,User.class);
                    User.updateUser(conn, user);
                    return"";
                })
        );

        Spark.delete("/user/:id",
                ((request, response) -> {
                    Integer id = Integer.valueOf(request.params("id"));
                    User.deleteUser(conn,id);

            return "";

                }));
    }
}
