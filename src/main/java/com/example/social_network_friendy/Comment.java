package com.example.social_network_friendy;

public class Comment {
    private String commentText;
    private String username;
    private long timestamp;

    public Comment() {
        // Default constructor required for Firebase
    }

    public Comment(String commentText, String username, long timestamp) {
        this.commentText = commentText;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getUsername() {
        return username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Add setters if needed
}
