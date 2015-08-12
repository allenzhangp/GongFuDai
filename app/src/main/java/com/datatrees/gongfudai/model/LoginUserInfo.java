package com.datatrees.gongfudai.model;

/**
 * Created by zhangping on 15/8/12.
 */
public class LoginUserInfo {
    private long userId;
    private String userName;
    private String phone;

    public LoginUserInfo() {
        userId = 10;
        userName = "";
        phone = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
