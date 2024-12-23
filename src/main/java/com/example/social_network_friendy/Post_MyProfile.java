package com.example.social_network_friendy;

import java.util.ArrayList;
import java.util.List;

public class Post_MyProfile {
    private String username;
    private String content;
    private String time;
    private int imageResId;
    private int likeCount;
    private boolean liked;
    private int id;


    public Post_MyProfile(int id, String username, String content, String time, int imageResId) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.time = time;
        this.imageResId = imageResId;
        this.likeCount = 0;
        this.liked = false;

    }

    public int getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public String getContent() {
        return content;
    }


    public String getTime() {
        return time;
    }


    public int getImageResId() {
        return imageResId;
    }

    public int getLikeCount() {
        return likeCount;
    }


    public boolean isLiked() {
        return liked;
    }



}
