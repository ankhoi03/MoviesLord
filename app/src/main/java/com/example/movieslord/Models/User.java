package com.example.movieslord.Models;

public class User {
    private String username;
    private String name;
    private String password;
    private String linkImg;

    public User() {
    }

    public User(String username, String name, String password, String linkImg) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.linkImg = linkImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }
}
