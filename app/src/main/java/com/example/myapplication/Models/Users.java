package com.example.myapplication.Models;

import java.util.HashMap;

public class Users {
    private String userName;
    private HashMap<String, User> allUsers = new HashMap<>();

    public Users() {
    }

    public String getUserName() {
        return userName;
    }

    public Users setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public HashMap<String, User> getAllUsers() {
        return allUsers;
    }

    public Users setAllUsers(HashMap<String, User> allUsers) {
        this.allUsers = allUsers;
        return this;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userName='" + userName + '\'' +
                ", allUsers=" + allUsers +
                '}';
    }
}
