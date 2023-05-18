package com.example.finalapplication_admin.models;

import java.io.Serializable;

public class OrderProductModel implements Serializable {
    String proName;
    String proImg;
    int proPrice;
    int proQty;
    int totalPrice;

    public OrderProductModel() {
    }

    public OrderProductModel(String proName, String proImg, int proPrice, int proQty, int totalPrice) {
        this.proName = proName;
        this.proImg = proImg;
        this.proPrice = proPrice;
        this.proQty = proQty;
        this.totalPrice = totalPrice;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public int getProPrice() {
        return proPrice;
    }

    public void setProPrice(int proPrice) {
        this.proPrice = proPrice;
    }

    public int getProQty() {
        return proQty;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
