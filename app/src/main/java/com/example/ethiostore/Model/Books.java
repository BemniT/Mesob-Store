package com.example.ethiostore.Model;

public class Books {

    private String sname, date, time, image, sid, description, apkFile;


    public Books() {
    }

    public Books(String sname, String date, String time, String image, String sid, String description, String apkFile) {
        this.sname = sname;
        this.date = date;
        this.time = time;
        this.image = image;
        this.sid = sid;
        this.description = description;
        this.apkFile = apkFile;
    }

    public String getApkFile() {
        return apkFile;
    }

    public void setApkFile(String apkFile) {
        this.apkFile = apkFile;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
