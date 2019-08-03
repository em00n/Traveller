package com.emon.traveller.model;

public class User {
   private String name,prourl,uid;

    public User() {
    }

    public User(String name, String prourl, String uid) {
        this.name = name;
        this.prourl = prourl;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProurl() {
        return prourl;
    }

    public void setProurl(String prourl) {
        this.prourl = prourl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
