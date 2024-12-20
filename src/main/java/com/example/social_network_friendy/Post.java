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
    // Constructor
    public Post(String postId,String username, String content, String timeAgo, List<String> imageBase64, int likeCount, int commentCount) {
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.timeAgo = timeAgo;
        this.imageBase64 = imageBase64;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
    public Post() {
        // Constructor mặc định cho Firebase
    }
    // Getter và Setter
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
        return timeAgo;
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
