package com.stanga.easypantry.enums;

import android.content.Context;
import com.stanga.easypantry.R;

public enum ItemsViewMode {
    GROUPED_BY_PANTRY(0, R.string.items_group_by_pantry),
    SHOW_ALL(1, R.string.items_show_all);

    private final int value;
    private final int stringResourceId;

    ItemsViewMode(int value, int stringResourceId) {
        this.value = value;
        this.stringResourceId = stringResourceId;
    }

    public int getValue() {
        return value;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public String getDisplayName(Context context) {
        return context.getString(stringResourceId);
    }

    public static ItemsViewMode fromValue(int value) {
        for (ItemsViewMode mode : values()) {
            if (mode.value == value) {
                return mode;
            }
        }
        return GROUPED_BY_PANTRY;
    }
}