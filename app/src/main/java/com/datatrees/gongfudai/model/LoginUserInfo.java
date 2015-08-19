package com.datatrees.gongfudai.model;

/**
 * Created by zhangping on 15/8/12.
 */
public class LoginUserInfo {
    private long userId;
    private String userName;
    private String token;

    public LoginUserInfo() {
        userId = 0;
        userName = "";
        token = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

}
