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
                //goes to the selectUser method and takes the info and serializes the object and return it to the page
                // because its a get you have a return
                //getting something back
                "/user",
                ((request, response) -> {
                    ArrayList<User>user = User.selectUser(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(user);
                })
        );
        Spark.post(
         //you are sending info back
        //jsonParser, takes the info that is entered by the user and inserts it into the table
                "/user",
                ((request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    User.insertUser(conn, user.username,user.address,user.email);
                    return "";
                })
        );
         //put is another way to say post to the form, therefore nothing is returned
         //updating the user info
        Spark.put("/user", (
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);
                    User.updateUser(conn, user);
                    //here we are putting the object user in
                    return"";
                })
        );
         //deleting the user
        //id is used to delete the user
        Spark.delete("/user/:id",
                ((request, response) -> {
                    Integer id = Integer.valueOf(request.params("id"));
                    User.deleteUser(conn,id);

            return "";

                })
        );
    }
}
