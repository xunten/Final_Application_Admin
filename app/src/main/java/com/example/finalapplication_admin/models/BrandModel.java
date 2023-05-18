package com.example.finalapplication_admin.models;

public class BrandModel {
    String img_url;
    String brandName;

    public BrandModel() {
    }

    public BrandModel(String img_url, String brandName) {
        this.img_url = img_url;
        this.brandName = brandName;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
