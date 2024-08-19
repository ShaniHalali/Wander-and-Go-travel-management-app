package com.example.myapplication.Models;

public class FirebaseUserModel {

    private String userKey;

    public FirebaseUserModel(String userKey) {
        this.userKey = userKey;
    }

    public FirebaseUserModel() {
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
