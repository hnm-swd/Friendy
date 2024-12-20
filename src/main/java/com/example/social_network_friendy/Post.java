////package com.example.social_network_friendy;
////
////
////public class Post {
////    private int id;
////    private String content;
////    private String imageUri; // Store imageUri as a String
////
////    public Post(String content, String imageUri) {
////        this.content = content;
////        this.imageUri = imageUri;
////    }
////
////    public int getId() {
////        return id;
////    }
////
////    public String getContent() {
////        return content;
////    }
////
////    public String getImageUri() {
////        return imageUri;
////    }
////
////    public void setId(int id) {
////        this.id = id;
////    }
////
////    public void setContent(String content) {
////        this.content = content;
////    }
////
////    public void setImageUri(String imageUri) {
////        this.imageUri = imageUri;
////    }
////    // Required empty constructor for Firebase
////    public Post() {
////    }
////
////    public Post(String content) {
////        this.content = content;
////    }
////
////
////}
////
////
//
//package com.example.social_network_friendy;
//
//import java.util.ArrayList;
//
//public class Post {
//
//    private String content;
//    private ArrayList<String> imageBase64;  // Danh sách ảnh dưới dạng Base64
//    private long timestamp;
//
//    // Constructor
//    public Post(String content, ArrayList<String> imageBase64, long timestamp) {
//        this.content = content;
//        this.imageBase64 = imageBase64;
//        this.timestamp = timestamp;
//    }
//
//    // Getter và Setter
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public ArrayList<String> getImageBase64() {
//        return imageBase64;
//    }
//
//    public void setImageBase64(ArrayList<String> imageBase64) {
//        this.imageBase64 = imageBase64;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }
//}
//
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

    // Constructor
    public Post(String username, String content, String timeAgo, List<String> imageBase64, int likeCount, int commentCount) {
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
}
