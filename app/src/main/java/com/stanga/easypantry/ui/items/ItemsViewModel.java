package com.stanga.easypantry.ui.items;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import com.stanga.easypantry.database.repositories.PantryItemRepository;
import com.stanga.easypantry.enums.ItemsViewMode;

import java.util.List;

public class ItemsViewModel extends AndroidViewModel {

    private static final String TAG = "ItemsViewModel";
    private final PantryItemRepository repository;
    private final MutableLiveData<ItemsViewMode> viewMode = new MutableLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public ItemsViewModel(Application application) {
        super(application);
        repository = new PantryItemRepository(application);

        // Default values
        viewMode.setValue(ItemsViewMode.GROUP_BY_PANTRY);
        searchQuery.setValue("");
        isLoading.setValue(false);

        Log.d(TAG, "ViewModel created with default GROUP_BY_PANTRY mode");
    }

    public LiveData<ItemsViewMode> getViewMode() {
        return viewMode;
    }

    public void setViewMode(ItemsViewMode mode) {
        Log.d(TAG, "setViewMode called: " + mode);
        if (!mode.equals(viewMode.getValue())) {
            viewMode.setValue(mode);
            Log.d(TAG, "ViewMode updated to: " + mode);
        } else {
            Log.d(TAG, "ViewMode unchanged: " + mode);
        }
    }

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String query) {
        Log.d(TAG, "setSearchQuery called: '" + query + "'");
        String currentQuery = searchQuery.getValue();
        if (!java.util.Objects.equals(currentQuery, query)) {
            searchQuery.setValue(query);
            Log.d(TAG, "SearchQuery updated to: '" + query + "'");
        }
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }

    public LiveData<List<PantryItemWithDetails>> getFilteredItems() {
        return Transformations.switchMap(searchQuery, query -> {
            Log.d(TAG, "getFilteredItems switchMap triggered with query: '" + query + "'");
            if (query == null || query.trim().isEmpty()) {
                Log.d(TAG, "Getting all items");
                return repository.getAllPantryItemsWithDetails();
            } else {
                Log.d(TAG, "Searching items with query: '" + query + "'");
                return repository.searchPantryItemsWithDetails(query);
            }
        });
    }

    // Repository methods
    public void insertItem(com.stanga.easypantry.database.entities.PantryItem pantryItem) {
        repository.insert(pantryItem);
    }

    public void updateItem(com.stanga.easypantry.database.entities.PantryItem pantryItem) {
        repository.update(pantryItem);
    }

    public void deleteItem(com.stanga.easypantry.database.entities.PantryItem pantryItem) {
        repository.delete(pantryItem);
    }
}