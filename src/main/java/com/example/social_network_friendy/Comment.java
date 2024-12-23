package com.example.social_network_friendy;

public class Comment {
    private String commentId;
    private String username;
    private String commentText;
    private long timestamp;

    public Comment() {
        // Default constructor required for Firebase
    }

    public Comment( String commentId, String username, String commentText, long timestamp) {
        this.commentId = commentId;
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }
    public String getCommentId() {
        return commentId;
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