package com.stanga.easypantry.database.entities;

import androidx.room.ColumnInfo;

public class PantryItemWithDetails {
    // PantryItem fields
    public int id;

    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "pantry_id")
    public int pantryId;

    public int quantity;
    public String unit;

    @ColumnInfo(name = "expiry_date")
    public Long expiryDate;

    public String notes;

    // Product details
    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "product_brand")
    public String productBrand;

    // Pantry details
    @ColumnInfo(name = "pantry_name")
    public String pantryName;

    public PantryItemWithDetails() {
        // Empty constructor required by Room
    }
}