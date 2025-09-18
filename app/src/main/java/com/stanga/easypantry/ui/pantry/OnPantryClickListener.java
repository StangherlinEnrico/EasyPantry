package com.stanga.easypantry.ui.pantry;

import com.stanga.easypantry.database.entities.Pantry;

public interface OnPantryClickListener {
    void onPantryClick(Pantry pantry);
    void onPantryLongClick(Pantry pantry);
}