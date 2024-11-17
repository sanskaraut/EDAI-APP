package com.cscorner.autohub;

public class WashingCenterOwner {
    private String name;
    private String username;
    private String mobileNo;

    public WashingCenterOwner() {
        // Required empty constructor
    }

    public WashingCenterOwner(String name, String username, String mobileNo) {
        this.name = name;
        this.username = username;
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getMobileNo() {
        return mobileNo;
    }
}