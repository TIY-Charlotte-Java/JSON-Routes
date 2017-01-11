package com.theironyard.charlotte;

public class User {
    Integer id = null;
    String userName;
    String address;
    String email;

    public User() {
    }

    public User(Integer id, String userName, String address, String email) {
        this.id = id;
        this.userName = userName;
        this.address = address;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
