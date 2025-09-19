package com.stanga.easypantry.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

@Entity(
        tableName = "pantry_items",
        foreignKeys = {
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "product_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Pantry.class,
                        parentColumns = "id",
                        childColumns = "pantry_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("product_id"), @Index("pantry_id")}
)
public class PantryItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "pantry_id")
    public int pantryId;

    @ColumnInfo(name = "quantity")
    public int quantity = 1000; // Store as int multiplied by 1000 for decimal precision

    // Add helper methods
    public float getQuantityAsFloat() {
        return quantity / 1000.0f;
    }

    public void setQuantityFromFloat(float value) {
        this.quantity = Math.round(value * 1000);
    }

    @ColumnInfo(name = "unit")
    @NonNull
    public String unit = "pz";

    @ColumnInfo(name = "expiry_date")
    public Date expiryDate;

    @ColumnInfo(name = "notes")
    public String notes;

    public PantryItem(int productId, int pantryId, int quantity, @NonNull String unit) {
        this.productId = productId;
        this.pantryId = pantryId;
        this.quantity = quantity;
        this.unit = unit;
    }
}