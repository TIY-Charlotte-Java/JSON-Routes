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
        stmt.execute("CREATE TABLE IF NOT EXISTS rforms (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, String username, String address, String email) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO rforms VALUES (NULL IDENTITY, ‘boothang’ VARCHAR, 192 Shook dr., boot@gmail.com)");
        stmt.execute("INSERT INTO rforms VALUES (NULL IDENTITY, ‘shampoo’ VARCHAR , 123 After dr.,  yo@yahoo.com)");
        stmt.execute("INSERT INTO rforms VALUES (NULL IDENTITY, ‘awesome’ VARCHAR, 34 Even Still ln., no@yahoo.com)");
        stmt.execute("INSERT INTO rforms VALUES (NULL IDENTITY, ‘pfchang’ VARCHAR, 54 Go dr., yes@go.com)");
    }

    public static void selectFrom(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT * FROM rforms WHERE username = ‘shampoo’");
    }
    public static void updateUser(Connection conn, String username, String address, String email) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("UPDATE rforms SET username = ‘conditioner’ WHERE username = ‘shampoo’");
    }
    public static void deleteUser(Connection conn, String username, String address, String email) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE rforms FROM WHERE username = ‘awesome’");
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        return users;
    }


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        selectFrom(conn);

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
            User usr = p.parse(body, User.class);
            insertUser(conn, usr.username, usr.address, usr.email);
            return "";

        })
	);
        Spark.put(
                "/user",
        ((request, response) -> {
            String body = request.body();
            JsonParser p = new JsonParser();
            User usr = p.parse(body, User.class);
            updateUser(conn, usr.username, usr.address, usr.email);
            return "";

        })
        );
        Spark.delete(
                "/user/:id",
        ((request, response) -> {
            request.params(":id");
            User usr = new User;
            deleteUser(conn, usr.username, usr.address, usr.email);
            return "";

        })
);

    }


}

