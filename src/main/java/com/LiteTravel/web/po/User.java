package com.LiteTravel.web.po;


public class User {
    public Integer userId;
    public String userCode;
    public String password;
    public Integer state;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + userId +
                ", user_code='" + userCode + '\'' +
                ", password='" + password + '\'' +
                ", user_state=" + state +
                '}';
    }
}