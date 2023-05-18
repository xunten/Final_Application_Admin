package com.example.finalapplication_admin.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    String id;
    String name;
    String phone;
    String email;
    String address;
    String date;
    int total;
    String status;
    List<OrderProductModel> products;

    public OrderModel() {
    }

    public OrderModel(String id, String name, String phone, String email, String address, String date, int total, String status, List<OrderProductModel> products) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.date = date;
        this.total = total;
        this.status = status;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductModel> products) {
        this.products = products;
    }
}
