package com.example.myapi.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapi.models.User;

public class SharedPrefManager {

    private static  final String SHARED_PREF_NAME = "my_shared_pref";
    private static  SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx = mCtx;
    }

    public static  synchronized  SharedPrefManager getInstance(Context mCtx){
        if (mInstance == null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return  mInstance;
    }

    public void  saveUser(User user){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("uid", user.getUid());
        editor.putString("uname", user.getUname());
        editor.putString("uphone", user.getUphone());
        editor.putString("uposition", user.getUposition());

        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.getInt("uid", -1) != -1){
            return true;
        }
        return false;
    }

    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User user = new User(
                sharedPreferences.getInt("uid", -1),
                sharedPreferences.getString("uname", null),
                sharedPreferences.getString("uphone", null),
                sharedPreferences.getString("uposition", null)
        );

        return  user;
    }

    public void logOut(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
