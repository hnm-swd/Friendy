package com.example.social_network_friendy;
public class Notification {
    private String type;       // Loại thông báo (like, comment, follow)
    private String message;    // Nội dung thông báo
    private String postId;     // ID bài viết
    private String senderId;   // ID người gửi
    private Object timestamp;  // Thời gian thông báo

    public Notification(String type, String message, String postId, String senderId, Object timestamp) {
        this.type = type;
        this.message = message;
        this.postId = postId;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getPostId() {
        return postId;
    }

    public String getSenderId() {
        return senderId;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

}
