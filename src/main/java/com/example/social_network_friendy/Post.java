package com.example.social_network_friendy;
import com.google.firebase.firestore.PropertyName;
import java.util.List;

public class Post {

    @PropertyName("username")
    private String username;

    @PropertyName("content")
    private String content;

    @PropertyName("timeAgo")
    private String timeAgo;

    @PropertyName("imageBase64")
    private List<String> imageBase64;

    @PropertyName("likeCount")
    private int likeCount;

    @PropertyName("commentCount")
    private int commentCount;

    @PropertyName("postId")
    private String postId;
    @PropertyName("hasUserLiked")
    private boolean hasUserLiked;

    @PropertyName("getTimestamp")
    private long timestamp;

    // Constructor
    public Post(String postId,String username, String content, long timestamp, List<String> imageBase64, int likeCount, int commentCount) {
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.imageBase64 = imageBase64;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
    public Post() {
        // Constructor mặc định cho Firebase
    }
    // Getter và Setter
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeAgo() {
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - timestamp;

        if (diff < 60 * 1000) {
            return "Vừa mới";
        } else if (diff < 60 * 60 * 1000) {
            return (diff / (60 * 1000)) + " phút trước";
        } else if (diff < 24 * 60 * 60 * 1000) {
            return (diff / (60 * 60 * 1000)) + " giờ trước";
        } else {
            return (diff / (24 * 60 * 60 * 1000)) + " ngày trước";
        }
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public List<String> getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(List<String> imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setHasUserLiked(boolean hasUserLiked) {
        this.hasUserLiked = hasUserLiked;
    }

    public boolean getHasUserLiked() {
        return hasUserLiked;
    }
}