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

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, address VARCHAR, email VARCHAR)");
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
                    ArrayList<User> users = User.selectUsers(conn);
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
                    User.insertUser(conn, user.name, user.address, user.email);
                    return "";
                })
        );

        Spark.put(
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    User.updateUser(conn, user.name, user.address, user.email, user.id);
                    return "";
                })
        );

        Spark.delete(
                "/user/:id",
                ((request, response) -> {
                    String idValue = request.params("id");
                    int indexNumber = Integer.valueOf(idValue);
                    User.deleteUser(conn, indexNumber);
                    return "";
                })
        );
    }

}
