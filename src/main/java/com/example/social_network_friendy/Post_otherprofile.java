package com.example.social_network_friendy;

public class Post_otherprofile {
    private String userName;
    private String timePosted;
    private String content;
    private int profileImage;
    private int postImage;
    private int likes;
    private int comments;

    public Post_otherprofile(String userName, String timePosted, String content, int profileImage, int postImage, int likes, int comments) {
        this.userName = userName;
        this.timePosted = timePosted;
        this.content = content;
        this.profileImage = profileImage;
        this.likes = likes;
        this.comments = comments;
        this.postImage=postImage;
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public String getContent() {
        return content;
    }

    public int getProfileImage() {
        return profileImage;
    }
    public int getPostImage() {
        return postImage;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }
}
