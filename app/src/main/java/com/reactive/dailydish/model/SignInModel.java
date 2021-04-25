package com.reactive.dailydish.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.reactive.dailydish.BR;

import java.io.Serializable;

public class SignInModel extends BaseObservable implements Serializable {
    String email;
    String password;
    boolean displayError;


    public SignInModel() {
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

    @Bindable
    public boolean isDisplayError(){
        return displayError;
    }

    public void setDisplayError(boolean displayError) {
        this.displayError = displayError;
        notifyPropertyChanged(BR.displayError);
    }

}
