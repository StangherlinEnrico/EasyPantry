package com.stanga.easypantry.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "pantry_items",
        foreignKeys = {
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "product_id",
                        onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = Pantry.class,
                        parentColumns = "id",
                        childColumns = "pantry_id",
                        onDelete = ForeignKey.RESTRICT)
        })
public class PantryItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "pantry_id")
    public int pantryId;

    @ColumnInfo(name = "quantity")
    @NonNull
    public int quantity = 0;

    @ColumnInfo(name = "expiry_date")
    public Date expiryDate; // nullable

    @ColumnInfo(name = "notes")
    public String notes; // nullable, max 200 chars

    public PantryItem(int productId, int pantryId, int quantity) {
        this.productId = productId;
        this.pantryId = pantryId;
        this.quantity = quantity;
    }
}