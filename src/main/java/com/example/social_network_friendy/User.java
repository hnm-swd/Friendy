package com.example.social_network_friendy;

public class User {
    private String userId;
    private String username;
    private String email;

    public User() {
        // Default constructor for Firebase
    }

    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}