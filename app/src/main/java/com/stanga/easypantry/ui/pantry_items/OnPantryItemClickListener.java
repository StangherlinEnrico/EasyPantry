package com.stanga.easypantry.ui.pantry_items;

import com.stanga.easypantry.database.entities.PantryItemWithDetails;

public interface OnPantryItemClickListener {
    void onPantryItemClick(PantryItemWithDetails item);
}