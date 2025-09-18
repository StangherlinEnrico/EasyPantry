package com.stanga.easypantry.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import com.stanga.easypantry.enums.LanguageMode;

import java.util.Locale;

public class LanguageHelper {
    private static final String PREFS_NAME = "language_prefs";
    private static final String LANGUAGE_KEY = "selected_language";

    public static void applyLanguage(Context context, LanguageMode languageMode) {
        Locale locale = languageMode.getLocale();

        if (locale != null) {
            Locale.setDefault(locale);
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocales(new LocaleList(locale));
            } else {
                configuration.setLocale(locale);
            }

            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        } else {
            // System default - reset to system locale
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocales(LocaleList.getDefault());
            } else {
                configuration.setLocale(Locale.getDefault());
            }

            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
    }

    public static void saveLanguage(Context context, LanguageMode languageMode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(LANGUAGE_KEY, languageMode.getLanguageCode()).apply();
    }

    public static LanguageMode getSavedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedLanguageCode = prefs.getString(LANGUAGE_KEY, LanguageMode.SYSTEM.getLanguageCode());
        return LanguageMode.fromLanguageCode(savedLanguageCode);
    }

    public static Context createLanguageContext(Context context) {
        LanguageMode savedLanguage = getSavedLanguage(context);
        if (savedLanguage == LanguageMode.SYSTEM) {
            return context;
        }

        Locale locale = savedLanguage.getLocale();
        if (locale == null) {
            return context;
        }

        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(locale));
        } else {
            configuration.setLocale(locale);
        }

        return context.createConfigurationContext(configuration);
    }
}