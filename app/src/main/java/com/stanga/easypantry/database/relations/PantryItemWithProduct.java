package com.stanga.easypantry.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.stanga.easypantry.database.entities.PantryItem;
import com.stanga.easypantry.database.entities.Product;

public class PantryItemWithProduct {
    @Embedded
    public PantryItem pantryItem;

    @Relation(
            parentColumn = "product_id",
            entityColumn = "id"
    )
    public Product product;
}