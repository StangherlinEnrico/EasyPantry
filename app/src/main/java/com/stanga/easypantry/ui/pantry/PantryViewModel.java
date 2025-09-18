package com.stanga.easypantry.ui.pantry;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import com.stanga.easypantry.database.entities.Pantry;
import com.stanga.easypantry.database.repositories.PantryRepository;

public class PantryViewModel extends AndroidViewModel {

    private PantryRepository repository;
    private LiveData<List<Pantry>> allPantries;

    public PantryViewModel(Application application) {
        super(application);
        repository = new PantryRepository(application);
        allPantries = repository.getAllPantries();
    }

    public LiveData<List<Pantry>> getAllPantries() {
        return allPantries;
    }

    public void insert(Pantry pantry) {
        repository.insert(pantry);
    }

    public void update(Pantry pantry) {
        repository.update(pantry);
    }

    public void delete(Pantry pantry) {
        repository.delete(pantry);
    }
}