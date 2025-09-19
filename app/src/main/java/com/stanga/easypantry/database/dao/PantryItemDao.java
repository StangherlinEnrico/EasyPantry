package com.stanga.easypantry.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stanga.easypantry.database.entities.PantryItem;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;

import java.util.List;

@Dao
public interface PantryItemDao {
    @Query("SELECT * FROM pantry_items")
    LiveData<List<PantryItem>> getAllPantryItems();

    @Query("SELECT * FROM pantry_items WHERE id = :id")
    LiveData<PantryItem> getPantryItemById(int id);

    @Query("SELECT * FROM pantry_items WHERE pantry_id = :pantryId")
    LiveData<List<PantryItem>> getItemsByPantryId(int pantryId);

    @Query("SELECT pi.*, p.name as product_name, p.brand as product_brand, pa.name as pantry_name " +
            "FROM pantry_items pi " +
            "JOIN products p ON pi.product_id = p.id " +
            "JOIN pantries pa ON pi.pantry_id = pa.id " +
            "ORDER BY pa.name, p.name")
    LiveData<List<PantryItemWithDetails>> getAllPantryItemsWithDetails();

    @Query("SELECT pi.*, p.name as product_name, p.brand as product_brand, pa.name as pantry_name " +
            "FROM pantry_items pi " +
            "JOIN products p ON pi.product_id = p.id " +
            "JOIN pantries pa ON pi.pantry_id = pa.id " +
            "WHERE p.name LIKE :searchQuery OR p.brand LIKE :searchQuery OR pa.name LIKE :searchQuery " +
            "ORDER BY pa.name, p.name")
    LiveData<List<PantryItemWithDetails>> searchPantryItemsWithDetails(String searchQuery);

    @Query("SELECT pi.*, p.name as product_name, p.brand as product_brand, pa.name as pantry_name " +
            "FROM pantry_items pi " +
            "JOIN products p ON pi.product_id = p.id " +
            "JOIN pantries pa ON pi.pantry_id = pa.id " +
            "WHERE pi.pantry_id = :pantryId " +
            "ORDER BY p.name")
    LiveData<List<PantryItemWithDetails>> getPantryItemsWithDetailsByPantryId(int pantryId);

    @Insert
    void insertPantryItem(PantryItem pantryItem);

    @Update
    void updatePantryItem(PantryItem pantryItem);

    @Delete
    void deletePantryItem(PantryItem pantryItem);

    @Query("DELETE FROM pantry_items WHERE id = :id")
    void deletePantryItemById(int id);
}