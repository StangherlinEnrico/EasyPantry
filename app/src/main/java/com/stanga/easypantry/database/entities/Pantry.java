package com.stanga.easypantry.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantries")
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "icon")
    public String icon;

    public Pantry(@NonNull String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
}
