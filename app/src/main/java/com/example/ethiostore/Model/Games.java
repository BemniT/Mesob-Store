package com.example.ethiostore.Model;

public class Games {

    public String apkFile, date, time, image, sid, sname, category, description;

    public Games() {
    }

    public Games(String apkFile, String date, String time, String image, String sid, String sname, String category, String description) {
        this.apkFile = apkFile;
        this.date = date;
        this.time = time;
        this.image = image;
        this.sid = sid;
        this.sname = sname;
        this.category = category;
        this.description = description;
    }

    public String getApkFile() {
        return apkFile;
    }

    public void setApkFile(String apkFile) {
        this.apkFile = apkFile;
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

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
