package com.theironyard.charlotte;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by meekinsworks on 9/2/16.
 */
public class Mainclass {

    public static void insertUser(Connection conn, Integer id, String username, String address, String email) throws SQLException {  
        PreparedStatement stm = conn.prepareStatement("INSERT INTO users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");  
        stm.setInt(1, id);          
        stm.setString(2, username);
          stm.setString(3, address);
          stm.setString(4, email);  
        stm.execute();       }  

    public static void createTables(Connection conn) throws SQLException { 
        Statement smt = conn.createStatement(); 
        smt.execute("CREATE TABLE users (id IDENTITY, username VARCHAR, address VARCHAR, email VARCHAR)");    

    }  

    public static ArrayList<User> selectUsers (Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();  
        PreparedStatement stm = conn.prepareStatement("SELECT * FROM users");  
        ResultSet results = stm.executeQuery();   
        while (results.next()) { 
            int id = results.getInt("id");  
            String username = results.getString("username");  
            String address = results.getString("address");              
            String email = results.getString("email");   

        }    return users;
         
             }     

    public static void main (String[]args) { //throws SQLException { 
//     Connection conn = DriverManager.getConnection("jdbc:h2:./main"); 
//        Server.createWebServer().start();   
//        createTables(conn); 
//          JsonParser p = new JsonParser();
//        User user= p.parse();
//
//        ArrayList<User> users = selectUsers(conn);
//        insertUser(conn, User.id, );   
//        Spark.externalStaticFileLocation("public");
//        Spark.init();
//
//        Spark.get(
//                "/user",  
//                        ((request, response) -> {  
//            ArrayList<User> users = selectUsers(conn);  
//            JsonSerializer js = new JsonSerializer();
//            return js.serialize(users);  
//        })  
//        );
//        Spark.put(  
//                "/user",
//                ((request, response) -> {  
//                    String body = request.body();  
//                    JsonParser p = new JsonParser();  
//                    User us = p.parse(body, User.class);   
//                    insertUser(conn, us.id, us.username, us.address, us.email ); // not sure why this doesn't work.    
//                   })  
//        );    
//    } 
}  

        }
