package com.stanga.easypantry.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import com.stanga.easypantry.enums.ThemeMode;

public class ThemeHelper {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String THEME_KEY = "selected_theme";

    public static void applyTheme(ThemeMode themeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode.getNightMode());
    }

    public static void saveTheme(Context context, ThemeMode themeMode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(THEME_KEY, themeMode.getValue()).apply();
    }

    public static ThemeMode getSavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedValue = prefs.getInt(THEME_KEY, ThemeMode.SYSTEM.getValue());
        return ThemeMode.fromValue(savedValue);
    }
}