package com.stanga.easypantry.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.stanga.easypantry.database.entities.Pantry;
import com.stanga.easypantry.database.entities.PantryItem;

import java.util.List;

public class PantryWithItems {
    @Embedded
    public Pantry pantry;

    @Relation(
            parentColumn = "id",
            entityColumn = "pantry_id"
    )
    public List<PantryItem> items;
}