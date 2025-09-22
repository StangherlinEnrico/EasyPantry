package com.stanga.easypantry.database.entities;

import androidx.room.ColumnInfo;
import java.io.Serializable;
import java.util.Date;

public class PantryItemWithDetails implements Serializable { // AGGIUNGI implements Serializable
    // Dati da pantry_items
    public int id;

    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "pantry_id")
    public int pantryId;

    public int quantity;

    @ColumnInfo(name = "expiry_date")
    public Date expiryDate;

    public String notes;

    // Dati da products (tramite JOIN)
    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "product_brand")
    public String productBrand;

    // Dati da pantries (tramite JOIN)
    @ColumnInfo(name = "pantry_name")
    public String pantryName;
}