package com.reactive.dailydish.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.reactive.dailydish.BR;
import com.reactive.dailydish.Utils.Constants;

import java.io.Serializable;

public class Kitchen extends BaseObservable implements Serializable {
    String chefId;
    String name;
    String image;
    String address;
    String likes="0";
    String sale="0";
    boolean displayError;

    public Kitchen() {
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Bindable
    public String getAddress() {
        if (address == null)
            return "";
        return address;
    }

    @Bindable
    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }


    @Bindable({"displayError","address"})
    public String getAddressError(){
        if (!isDisplayError()){
            return "";
        }
        if (getAddress().isEmpty()){
            return "Address Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
        notifyPropertyChanged(BR.likes);
    }


    @Bindable({"displayError","likes"})
    public String getLikesError(){
        if (!isDisplayError()){
            return "";
        }
        if (getLikes().isEmpty()){
            return "Like Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getSale() {
        if (sale == null)
            return "";
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
        notifyPropertyChanged(BR.sale);
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
        return "Kitchen{" +
                "chefId='" + chefId + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", likes='" + likes + '\'' +
                ", displayError=" + displayError +
                '}';
    }
}
