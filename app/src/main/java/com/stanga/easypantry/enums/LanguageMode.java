package com.stanga.easypantry.enums;

import android.content.Context;
import com.stanga.easypantry.R;
import java.util.Locale;

public enum LanguageMode {
    SYSTEM("system", R.string.language_system),
    ENGLISH("en", R.string.language_english),
    ITALIAN("it", R.string.language_italian);

    private final String languageCode;
    private final int stringResourceId;

    LanguageMode(String languageCode, int stringResourceId) {
        this.languageCode = languageCode;
        this.stringResourceId = stringResourceId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public String getDisplayName(Context context) {
        return context.getString(stringResourceId);
    }

    public static LanguageMode fromLanguageCode(String languageCode) {
        for (LanguageMode language : values()) {
            if (language.languageCode.equals(languageCode)) {
                return language;
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

    public Locale getLocale() {
        switch (this) {
            case ENGLISH:
                return new Locale("en");
            case ITALIAN:
                return new Locale("it");
            case SYSTEM:
            default:
                return null; // System default
        }
    }
}