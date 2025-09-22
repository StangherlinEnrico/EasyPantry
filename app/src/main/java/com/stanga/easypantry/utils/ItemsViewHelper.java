package com.stanga.easypantry.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.stanga.easypantry.enums.ItemsViewMode;

public class ItemsViewHelper {
    private static final String PREFS_NAME = "items_view_prefs";
    private static final String VIEW_MODE_KEY = "items_view_mode";

    public static void saveItemsViewMode(Context context, ItemsViewMode viewMode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(VIEW_MODE_KEY, viewMode.getValue()).apply();
    }

    public static ItemsViewMode getSavedItemsViewMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedValue = prefs.getInt(VIEW_MODE_KEY, ItemsViewMode.GROUPED_BY_PANTRY.getValue());
        return ItemsViewMode.fromValue(savedValue);
    }
}