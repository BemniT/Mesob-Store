package com.example.ethiostore.Model;

public class Apps {

    public String apkFile, date, time, image, sid, sname;
    public Apps() {
    }

    public Apps(String apkFile, String date, String time, String image, String sid, String sname) {
        this.apkFile = apkFile;
        this.date = date;
        this.time = time;
        this.image = image;
        this.sid = sid;
        this.sname = sname;
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
}
