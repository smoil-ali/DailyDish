package com.reactive.dailydish.model;

import java.io.Serializable;
import java.util.List;

public class Checkout implements Serializable {
    String id;
    List<Cart> cart;
    String grandTotal;
    ProfileModel model;
    String status;
    String address;

    public Checkout() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public ProfileModel getModel() {
        return model;
    }

    public void setModel(ProfileModel model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
