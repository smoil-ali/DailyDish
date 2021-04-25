package com.reactive.dailydish.model;

public class Like {
    String userId;
    boolean status;

    public Like() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Like{" +
                "userId='" + userId + '\'' +
                ", status=" + status +
                '}';
    }
}
