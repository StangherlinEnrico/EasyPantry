package com.stanga.easypantry.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stanga.easypantry.database.entities.Pantry;

import java.util.List;

@Dao
public interface PantryDao {
    @Query("SELECT * FROM pantries")
    LiveData<List<Pantry>> getAllPantries();

    @Query("SELECT * FROM pantries WHERE id = :id")
    LiveData<Pantry> getPantryById(int id);

    @Insert
    void insertPantry(Pantry pantry);

    @Update
    void updatePantry(Pantry pantry);

    @Delete
    void deletePantry(Pantry pantry);

    @Query("DELETE FROM pantries WHERE id = :id")
    void deletePantryById(int id);
}
