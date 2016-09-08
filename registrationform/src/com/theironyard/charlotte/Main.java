package com.theironyard.charlotte;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS rforms (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertInfo(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO rforms VALUES (NULL, ‘boothang’, 192 Shook dr., boot@gmail.com)");
        stmt.execute("INSERT INTO rforms VALUES (NULL, ‘shampoo’ , 123 After dr.,  yo@yahoo.com)");
        stmt.execute("INSERT INTO rforms VALUES (NULL, ‘awesome’, 34 Even Still ln., no@yahoo.com)");
        stmt.execute("INSERT INTO rforms VALUES (NULL, ‘pfchang’, 54 Go dr., yes@go.com)");
    }

    public static void selectFrom(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("SELECT * FROM rforms WHERE username = ‘shampoo’)");
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        insertInfo(conn);
        selectFrom(conn);

    }
}

