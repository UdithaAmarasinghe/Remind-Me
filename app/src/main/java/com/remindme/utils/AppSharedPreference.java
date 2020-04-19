package com.remindme.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.remindme.R;
import com.remindme.models.Token;
import com.remindme.models.User;
import com.remindme.services.responses.TokenResponse;

import static com.remindme.utils.Constant.PREF_AUTHENTICATION_KEY;
import static com.remindme.utils.Constant.PREF_REFRESH_KEY;
import static com.remindme.utils.Constant.PREF_USER;

public class AppSharedPreference {

    private static Context mContext;
    private static SharedPreferences mPreferences;

    public AppSharedPreference(Context context) {
        mContext = context;
        init();
    }

    private static void init() {
        mPreferences = mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE);
    }
    public void clearSharefPreference() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void setUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_USER, userJson);
        editor.commit();
    }

    public User getUser() {
        Gson gson = new Gson();
        String json = mPreferences.getString(PREF_USER, "");
        User user = gson.fromJson(json, User.class);
        return user;
    }


    public void setRefershToken(String refreshKey) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_REFRESH_KEY, refreshKey);
        editor.commit();
    }

    public String getRefershToken() {
        return mPreferences.getString(PREF_REFRESH_KEY, "");
    }

    public void setToken(String accessToken) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_AUTHENTICATION_KEY, accessToken);
        editor.commit();
    }

    public String getToken() {
        return mPreferences.getString(PREF_AUTHENTICATION_KEY, "");
    }


}
