package com.example.social_network_friendy;
public class Comment {
    private String commentId;
    private String username;
    private String commentText;
    private long timestamp;
    private String avatarUrl; // Store the avatar URL

    // No-argument constructor (required by Firebase)
    public Comment() {
        // Firebase requires an empty constructor for deserialization
    }

    // Constructor with arguments
    public Comment(String username, String commentText, long timestamp, String avatarUrl) {
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.avatarUrl = avatarUrl; // Save the avatar URL
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    // Add setters if needed
}
