package com.reactive.dailydish.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.reactive.dailydish.BR;

import java.io.Serializable;

public class Menu extends BaseObservable implements Serializable {
    String id;
    String kitchenId;
    String name;
    String rating;
    String offers;
    String price;
    String discount;
    String category;
    String image;
    boolean displayError;

    public Menu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId;
    }

    @Bindable
    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable({"displayError","name"})
    public String getNameError(){
        if (!isDisplayError()){
            return "";
        }
        if (getName().isEmpty()){
            return "Name Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getRating() {
        if (rating == null)
            return "";
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
        notifyPropertyChanged(BR.rating);
    }

    @Bindable({"displayError","rating"})
    public String getRatingError(){
        if (!isDisplayError()){
            return "";
        }
        if (getRating().isEmpty()){
            return "Rating Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getOffers() {
        if (offers == null)
            return "";
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
        notifyPropertyChanged(BR.offers);
    }

    @Bindable({"displayError","offers"})
    public String getOffersError(){
        if (!isDisplayError()){
            return "";
        }
        return "";
    }

    @Bindable
    public String getPrice() {
        if (price == null)
            return "";
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable({"displayError","price"})
    public String getPriceError(){
        if (!isDisplayError()){
            return "";
        }
        if (getPrice().isEmpty()){
            return "Price Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getDiscount() {
        if (discount == null)
            return "";
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
        notifyPropertyChanged(BR.discount);
    }

    @Bindable({"displayError","discount"})
    public String getDiscountError(){
        if (!isDisplayError()){
            return "";
        }
        if (getDiscount().isEmpty()){
            return "Discount Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getCategory() {
        if (category == null)
            return "";
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable({"displayError","category"})
    public String getCategoryError(){
        if (!isDisplayError()){
            return "";
        }
        if (getCategory().isEmpty()){
            return "Category Field is Empty";
        }
        return "";
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Bindable
    public boolean isDisplayError(){
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", kitchenId='" + kitchenId + '\'' +
                ", name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                ", offers='" + offers + '\'' +
                ", price='" + price + '\'' +
                ", discount='" + discount + '\'' +
                ", category='" + category + '\'' +
                ", image='" + image + '\'' +
                ", displayError=" + displayError +
                '}';
    }
}
