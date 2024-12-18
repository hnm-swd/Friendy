package com.example.social_network_friendy;

public class NotificationItem {
    private String username;
    private String comment;
    private int likeCount;
    private int avatarResId;

    public NotificationItem(String username, String comment, int likeCount, int avatarResId) {
        this.username = username;
        this.comment = comment;
        this.likeCount = likeCount;
        this.avatarResId = avatarResId;
    }

    // Getter và Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }
}
