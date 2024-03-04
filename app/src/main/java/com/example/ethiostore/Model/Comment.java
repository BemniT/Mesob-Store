package com.example.ethiostore.Model;

public class Comment {
    private String userName, userImage;
    private String comment, timestamp;

    public Comment() {
    }

    public Comment(String userName, String comment, String timestamp, String userImage) {
        this.userName = userName;
        this.comment = comment;
        this.timestamp = timestamp;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}