package com.reactive.dailydish.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.reactive.dailydish.BR;

import java.io.Serializable;

public class ProfileModel extends BaseObservable implements Serializable {
    String id;
    String name;
    String email;
    String password;
    String image;
    boolean displayError;

    public ProfileModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public String getEmail() {
        if (email == null)
            return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable({"displayError","email"})
    public String getEmailError(){
        if (!isDisplayError()){
            return "";
        }

        if (getEmail().isEmpty()){
            return "Email Field is Empty";
        }
        return "";
    }

    @Bindable
    public String getPassword() {
        if (password == null)
            return "";
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable({"displayError","password"})
    public String getPasswordError(){
        if (!isDisplayError()){
            return "";
        }

        if (getPassword().isEmpty()){
            return "Password Field is Empty";
        }
        return "";
    }


    public String getImage() {
        if (image == null)
            return "";
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
        return "ProfileModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", displayError=" + displayError +
                '}';
    }
}
