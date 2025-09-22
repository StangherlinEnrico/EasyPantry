package com.stanga.easypantry.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.stanga.easypantry.database.AppDatabase;
import com.stanga.easypantry.database.dao.PantryItemDao;
import com.stanga.easypantry.database.entities.PantryItem;
import java.util.List;

public class PantryItemRepository {
    private final PantryItemDao pantryItemDao;
    private final LiveData<List<PantryItem>> allPantryItems;

    public PantryItemRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        pantryItemDao = database.pantryItemDao();
        allPantryItems = pantryItemDao.getAllPantryItems();
    }

    public LiveData<List<PantryItem>> getAllPantryItems() {
        return allPantryItems;
    }

    public LiveData<PantryItem> getPantryItemById(int id) {
        return pantryItemDao.getPantryItemById(id);
    }

    public LiveData<List<PantryItem>> getPantryItemsByPantryId(int pantryId) {
        return pantryItemDao.getPantryItemsByPantryId(pantryId);
    }

    public LiveData<List<PantryItem>> getPantryItemsByProductId(int productId) {
        return pantryItemDao.getPantryItemsByProductId(productId);
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

    // Verifica se un prodotto può essere eliminato
    public boolean canDeleteProduct(int productId) {
        return pantryItemDao.getProductUsageCount(productId) == 0;
    }

    // Verifica se una dispensa può essere eliminata
    public boolean canDeletePantry(int pantryId) {
        return pantryItemDao.getPantryUsageCount(pantryId) == 0;
    }
}