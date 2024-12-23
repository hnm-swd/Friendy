package com.example.social_network_friendy;

public class USER_FINDING {
    private int imagefinding;
    private String namefinding;

    public int getImagefinding() {
        return imagefinding;
    }

    public void setImagefinding(int imagefinding) {
        this.imagefinding = imagefinding;
    }

    public String getNamefinding() {
        return namefinding;
    }

    public void setNamefinding(String namefinding) {
        this.namefinding = namefinding;
    }

    public USER_FINDING(int imagefinding, String namefinding) {
        this.imagefinding = imagefinding;
        this.namefinding = namefinding;
    }

}
