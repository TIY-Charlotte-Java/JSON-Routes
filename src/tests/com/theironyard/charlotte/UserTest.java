package com.theironyard.charlotte;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by mfahrner on 8/31/16.
 */
public class UserTest {

    public Connection startConnection() throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
    Main.createTables(conn);
    return conn;
    }


    @Test
    public void insertUserANDselectUser() throws Exception {
        Connection conn = startConnection();
        User.insertUser(conn, "mike", "street", "yamama@yamama.com");
        User user = User.selectUser(conn, "mike");
        conn.close();
        assertTrue(user != null);
    }

    @Test
    public void deleteUser() throws Exception {
        Connection conn = startConnection();
        User.insertUser(conn, "mike", "street", "yamama@yamama.com");
        User user = User.selectUser(conn, "mike");
    }

    @Test
    public void updateUser() throws Exception {
        Connection conn = startConnection();
        User.insertUser(conn, "mike", "street", "yamama@yamama.com");
        User.updateUser(conn, "butts", "fake", "fake@fake.com", 1);
        User user = User.selectUser(conn, "butts");
        conn.close();
        assertEquals(user.name, "butts");
    }

}