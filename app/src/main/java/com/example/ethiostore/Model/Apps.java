package com.example.ethiostore.Model;

public class Apps {

    public String apk_file, categories, app_icon_url, app_id, app_name, app_description, developer_email,developer_phone
            , developer_name,app_size, image_1, image_2,image_3,type;

    //private long timestamp;
    public Apps() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Apps(String apk_file, String type, String categories, String app_icon_url, String app_id, String app_name, String app_description, String developer_email, String developer_phone, String developer_name, String app_size, String image_1, String image_2, String image_3, long timestamp) {
        this.apk_file = apk_file;
        this.categories = categories;
        this.app_icon_url = app_icon_url;
        this.app_id = app_id;
        this.app_name = app_name;
        this.app_description = app_description;
        this.developer_email = developer_email;
        this.developer_phone = developer_phone;
        this.developer_name = developer_name;
        this.app_size = app_size;
        this.image_1 = image_1;
        this.image_2 = image_2;
        this.image_3 = image_3;
//        this.timestamp = timestamp;
        this.type = type;
    }

    public String getApk_file() {
        return apk_file;
    }

    public void setApk_file(String apk_file) {
        this.apk_file = apk_file;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getApp_icon_url() {
        return app_icon_url;
    }

    public void setApp_icon_url(String app_icon_url) {
        this.app_icon_url = app_icon_url;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_description() {
        return app_description;
    }

    public void setApp_description(String app_description) {
        this.app_description = app_description;
    }

    public String getDeveloper_email() {
        return developer_email;
    }

    public void setDeveloper_email(String developer_email) {
        this.developer_email = developer_email;
    }

    public String getDeveloper_phone() {
        return developer_phone;
    }

    public void setDeveloper_phone(String developer_phone) {
        this.developer_phone = developer_phone;
    }

    public String getDeveloper_name() {
        return developer_name;
    }

    public void setDeveloper_name(String developer_name) {
        this.developer_name = developer_name;
    }

    public String getApp_size() {
        return app_size;
    }

    public void setApp_size(String app_size) {
        this.app_size = app_size;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    public String getImage_3() {
        return image_3;
    }

    public void setImage_3(String image_3) {
        this.image_3 = image_3;
    }

//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }
}
