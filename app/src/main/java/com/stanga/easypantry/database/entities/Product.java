package com.stanga.easypantry.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "products")
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "brand")
    public String brand;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @Ignore
    public Product(@NonNull String name) {
        this.name = name;
    }

    public Product(@NonNull String name, String brand) {
        this.name = name;
        this.brand = brand;
    }
}