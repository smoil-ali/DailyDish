package com.reactive.dailydish.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.reactive.dailydish.BR;

import java.io.Serializable;
import java.util.List;

public class Cart extends BaseObservable implements  Serializable {
    Menu menu;
    String total;
    String discountedPrice;
    String qty = "1";

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Bindable({"qty","discountedPrice"})
    public String getTotal() {
        if (!getQty().isEmpty()){
            total = getDiscountedPrice();
            int qty = Integer.parseInt(getQty());
            double val = qty * Double.parseDouble(total);
            total = String.valueOf(val);
        }
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public String getQty() {
        if (qty == null)
            return "1";
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
        notifyPropertyChanged(BR.qty);
    }

    @Bindable
    public String getDiscountedPrice() {
        if (discountedPrice == null)
            return "";
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
        notifyPropertyChanged(BR.discountedPrice);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "menu=" + menu +
                ", total='" + total + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }
}
