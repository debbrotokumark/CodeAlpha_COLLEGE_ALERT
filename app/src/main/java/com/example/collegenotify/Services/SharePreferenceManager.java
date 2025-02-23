package com.example.collegenotify.Services;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceManager {
    private static final String PREF_NAME = "com.example.collegenotify.Services";
    private static final String FIRST_TIME_KEY = "isFirstTime";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharePreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Check if the app is launched for the first time
    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(FIRST_TIME_KEY, true);
    }

    public void setFirstTime(boolean isFirstTime) {
        editor.putBoolean(FIRST_TIME_KEY, isFirstTime);
        editor.apply();
    }
}
