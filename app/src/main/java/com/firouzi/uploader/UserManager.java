package com.firouzi.uploader;


import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private final SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences("user_information", Context.MODE_PRIVATE);

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });
    }

    public void saveshow(Boolean show) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("show", show);
        editor.apply();
    }

    public Boolean getShow() {
        return sharedPreferences.getBoolean("show", false);
    }

}

