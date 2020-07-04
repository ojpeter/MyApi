package com.example.myapi.models;

public class User {

    private int uid;
    private String uname, uphone, uposition;

    public User(int uid, String uname, String uphone, String uposition) {
        this.uid = uid;
        this.uname = uname;
        this.uphone = uphone;
        this.uposition = uposition;
    }

    public int getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public String getUphone() {
        return uphone;
    }

    public String getUposition() {
        return uposition;
    }
}
