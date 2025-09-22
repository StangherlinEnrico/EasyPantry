package com.stanga.easypantry.ui.pantry_items;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import com.stanga.easypantry.database.repositories.PantryItemRepository;
import com.stanga.easypantry.enums.ItemsViewMode;
import com.stanga.easypantry.utils.ItemsViewHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PantryItemViewModel extends AndroidViewModel {
    private PantryItemRepository repository;
    private LiveData<List<PantryItemWithDetails>> allPantryItems;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();

    public PantryItemViewModel(Application application) {
        super(application);
        repository = new PantryItemRepository(application);
        allPantryItems = repository.getAllPantryItemsWithDetails();

        // Set initial empty search query
        searchQuery.setValue("");
    }

    public LiveData<List<PantryItemWithDetails>> getAllPantryItems() {
        return allPantryItems;
    }

    public LiveData<List<PantryItemWithDetails>> getSearchResults() {
        return Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                return allPantryItems;
            } else {
                return repository.searchPantryItems(query);
            }
        });
    }

    public LiveData<List<PantryGroupAdapter.PantryGroup>> getGroupedSearchResults() {
        return Transformations.map(getSearchResults(), items -> {
            if (items == null) return new ArrayList<>();

            Map<String, List<PantryItemWithDetails>> grouped = items.stream()
                    .collect(Collectors.groupingBy(item -> item.pantryName));

            return grouped.entrySet().stream()
                    .map(entry -> new PantryGroupAdapter.PantryGroup(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        });
    }

    public void setSearchQuery(String query) {
        String currentQuery = searchQuery.getValue();
        if (!java.util.Objects.equals(currentQuery, query)) {
            searchQuery.setValue(query);
        }
    }

    public ItemsViewMode getCurrentViewMode() {
        return ItemsViewHelper.getSavedItemsViewMode(getApplication());
    }
}