package com.theironyard.charlotte;

import org.junit.Test;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by jenniferchang on 8/31/16.
 */
public class DataRepositoryTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }

    //test for insert and select user
    @Test
    public void testInsertSelect() throws SQLException {
        Connection conn = startConnection();
        DataRepository.insertUser(conn, "Alice", "333 generic lane", "alice@gmail.com");
        DataRepository.insertUser(conn, "Bob", "333 generic road", "bob@gmail.com");
        DataRepository.insertUser(conn, "Charlie", "333 generic way", "charlie@gmail.com");
        ArrayList<User> users = DataRepository.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 3);
    }

    @Test
    public void testUpdate() throws SQLException {
        Connection conn = startConnection();
        DataRepository.insertUser(conn, "Alice", "333 generic lane", "alice@gmail.com");
        ArrayList<User> user = DataRepository.selectUsers(conn);
        int id = user.get(0).getId();
        DataRepository.updateUser(conn, "change", "change", "change", id);


    }

    @Test
    public void testDelete() throws SQLException {
        Connection conn = startConnection();
        DataRepository.insertUser(conn, "Alice", "333 generic lane", "alice@gmail.com");
        DataRepository.insertUser(conn, "Bob", "333 generic road", "bob@gmail.com");
        DataRepository.insertUser(conn, "Charlie", "333 generic way", "charlie@gmail.com");
        DataRepository.deleteUser(conn, 1);
        ArrayList<User> users = DataRepository.selectUsers(conn);
        conn.close();
        assertTrue(users.size() == 2);
    }

}