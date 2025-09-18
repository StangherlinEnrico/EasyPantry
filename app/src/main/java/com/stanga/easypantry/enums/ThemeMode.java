package com.stanga.easypantry.enums;

import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import com.stanga.easypantry.R;

public enum ThemeMode {
    LIGHT(0, R.string.theme_light, AppCompatDelegate.MODE_NIGHT_NO),
    DARK(1, R.string.theme_dark, AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM(2, R.string.theme_system, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    private final int value;
    private final int stringResourceId;
    private final int nightMode;

    ThemeMode(int value, int stringResourceId, int nightMode) {
        this.value = value;
        this.stringResourceId = stringResourceId;
        this.nightMode = nightMode;
    }

    public int getValue() {
        return value;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public int getNightMode() {
        return nightMode;
    }

    public String getDisplayName(Context context) {
        return context.getString(stringResourceId);
    }

    public static ThemeMode fromValue(int value) {
        for (ThemeMode theme : values()) {
            if (theme.value == value) {
                return theme;
            }
        }
        return SYSTEM; // Default fallback
    }

    public static String[] getDisplayNames(Context context) {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            names[i] = values()[i].getDisplayName(context);
        }
        return names;
    }
}