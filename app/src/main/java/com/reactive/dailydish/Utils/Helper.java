package com.reactive.dailydish.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reactive.dailydish.model.Cart;
import com.reactive.dailydish.model.Kitchen;
import com.reactive.dailydish.model.Menu;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Helper {
    static String SHARED_PREFERENCES = "sharedPreferences";
    static String CART = "cart";
    static String USER = "user";
    static String CART_DATA = "CardData";
    static String IS_LOGIN = "is_login";
    static String USER_INFO = "user_info";


    public static void setLogin(Context context,boolean isLogin){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SHARED_PREFERENCES,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGIN,isLogin);
        editor.apply();
    }

    public static boolean IsLogin(Context context){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SHARED_PREFERENCES,0);
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public static void setUser(Context context,String user){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(USER,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_INFO,user);
        editor.apply();
    }

    public static String getUser(Context context){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(USER,0);
        return sharedPreferences.getString(USER_INFO,"");
    }

    public static String fromKitchenToString(Kitchen model){
        if (model == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Kitchen>() {
        }.getType();
        String json = gson.toJson(model, type);
        return json;
    }

    public static Kitchen fromStringToKitchen(String model){
        if (model == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Kitchen>() {
        }.getType();
        Kitchen kitchen = gson.fromJson(model, type);
        return kitchen;
    }

    public static String fromListToString(List<Menu> menus){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Menu>>(){}.getType();
        String json = gson.toJson(menus,type);
        return json;
    }

    public static List<Menu> fromStringToList(String list){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Menu>>(){}.getType();
        List<Menu> menuList = gson.fromJson(list,type);
        return menuList;
    }

    public static void setCartData(String list,Context context){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(CART,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CART_DATA,list);
        editor.apply();
    }

    public static String getCartData(Context context){
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(CART,0);
        return sharedPreferences.getString(CART_DATA,"");
    }
}
