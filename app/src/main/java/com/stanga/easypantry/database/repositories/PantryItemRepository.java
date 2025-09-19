package com.stanga.easypantry.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.stanga.easypantry.database.AppDatabase;
import com.stanga.easypantry.database.dao.PantryItemDao;
import com.stanga.easypantry.database.entities.PantryItem;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;

import java.util.List;

public class PantryItemRepository {
    private final PantryItemDao pantryItemDao;
    private final LiveData<List<PantryItem>> allPantryItems;
    private final LiveData<List<PantryItemWithDetails>> allPantryItemsWithDetails;

    public PantryItemRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        pantryItemDao = database.pantryItemDao();
        allPantryItems = pantryItemDao.getAllPantryItems();
        allPantryItemsWithDetails = pantryItemDao.getAllPantryItemsWithDetails();
    }

    public LiveData<List<PantryItem>> getAllPantryItems() {
        return allPantryItems;
    }

    public LiveData<List<PantryItemWithDetails>> getAllPantryItemsWithDetails() {
        return allPantryItemsWithDetails;
    }

    public LiveData<PantryItem> getPantryItemById(int id) {
        return pantryItemDao.getPantryItemById(id);
    }

    public LiveData<List<PantryItem>> getItemsByPantryId(int pantryId) {
        return pantryItemDao.getItemsByPantryId(pantryId);
    }

    public LiveData<List<PantryItemWithDetails>> getPantryItemsWithDetailsByPantryId(int pantryId) {
        return pantryItemDao.getPantryItemsWithDetailsByPantryId(pantryId);
    }

    public LiveData<List<PantryItemWithDetails>> searchPantryItemsWithDetails(String searchQuery) {
        return pantryItemDao.searchPantryItemsWithDetails("%" + searchQuery + "%");
    }

    public void insert(PantryItem pantryItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.insertPantryItem(pantryItem);
        });
    }

    public void update(PantryItem pantryItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.updatePantryItem(pantryItem);
        });
    }

    public void delete(PantryItem pantryItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.deletePantryItem(pantryItem);
        });
    }
}