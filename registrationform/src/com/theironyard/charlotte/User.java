package com.theironyard.charlotte;

/**
 * Created by meekinsworks on 9/8/16.
 */
public class User {
    String username;
    Integer id;
    String address;
    String email;

    public User(String username, Integer id, String address, String email) {
        this.username = username;
        this.id = id;
        this.address = address;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
