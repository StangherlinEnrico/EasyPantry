package com.stanga.easypantry.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.stanga.easypantry.database.AppDatabase;
import com.stanga.easypantry.database.dao.PantryDao;
import com.stanga.easypantry.database.entities.Pantry;

import java.util.List;

public class PantryRepository {
    private final PantryDao pantryDao;
    private final LiveData<List<Pantry>> allPantries;

    public PantryRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        pantryDao = database.pantryDao();
        allPantries = pantryDao.getAllPantries();
    }

    public LiveData<List<Pantry>> getAllPantries() {
        return allPantries;
    }

    public LiveData<Pantry> getPantryById(int id) {
        return pantryDao.getPantryById(id);
    }

    public void insert(Pantry pantry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryDao.insertPantry(pantry);
        });
    }

    public void update(Pantry pantry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryDao.updatePantry(pantry);
        });
    }

    public void delete(Pantry pantry) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryDao.deletePantry(pantry);
        });
    }
}
