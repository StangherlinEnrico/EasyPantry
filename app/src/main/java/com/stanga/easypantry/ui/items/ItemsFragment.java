package com.stanga.easypantry.ui.items;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.stanga.easypantry.R;
import com.stanga.easypantry.databinding.FragmentItemsBinding;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import com.stanga.easypantry.enums.ItemsViewMode;
import com.stanga.easypantry.ui.items.utils.GroupingHelper;

import java.util.List;

public class ItemsFragment extends Fragment {

    private static final String TAG = "ItemsFragment";
    private FragmentItemsBinding binding;
    private ItemsViewModel itemsViewModel;
    private ItemsAdapter itemsAdapter;
    private GroupedItemsAdapter groupedItemsAdapter;
    private List<PantryItemWithDetails> currentItems;

    // Common click listener for both adapters
    private final ItemClickHandler itemClickHandler = new ItemClickHandler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);

        binding = FragmentItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        setupFilterChips();
        setupSearch();
        setupObservers();
        setupFab();

        return root;
    }

    private void setupRecyclerView() {
        // Setup both adapters with the common handler
        itemsAdapter = new ItemsAdapter(itemClickHandler);
        groupedItemsAdapter = new GroupedItemsAdapter(itemClickHandler);

        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
        // Start with grouped adapter (default mode)
        binding.recyclerViewItems.setAdapter(groupedItemsAdapter);
    }

    private void setupFilterChips() {
        binding.filterChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            int checkedId = checkedIds.get(0);
            ItemsViewMode newMode;

            if (checkedId == R.id.chip_group_by_pantry) {
                newMode = ItemsViewMode.GROUP_BY_PANTRY;
            } else {
                newMode = ItemsViewMode.SHOW_ALL_ITEMS;
            }

            Log.d(TAG, "Filter chip changed to: " + newMode);
            itemsViewModel.setViewMode(newMode);
        });
    }

    private void setupSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemsViewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsViewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupObservers() {
        itemsViewModel.getViewMode().observe(getViewLifecycleOwner(), viewMode -> {
            Log.d(TAG, "ViewMode changed to: " + viewMode);
            updateChipSelection(viewMode);
            updateDisplay(viewMode, currentItems);
        });

        itemsViewModel.getFilteredItems().observe(getViewLifecycleOwner(), items -> {
            Log.d(TAG, "Items received: " + (items != null ? items.size() : "null"));
            currentItems = items;
            ItemsViewMode currentMode = itemsViewModel.getViewMode().getValue();
            updateDisplay(currentMode, items);
        });

        itemsViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            Log.d(TAG, "Loading state: " + isLoading);
        });
    }

    private void setupFab() {
        binding.fabAddItem.setOnClickListener(v -> {
            // WIP - Add item functionality
        });
    }

    private void updateDisplay(ItemsViewMode viewMode, List<PantryItemWithDetails> items) {
        Log.d(TAG, "updateDisplay - viewMode: " + viewMode + ", items: " + (items != null ? items.size() : "null"));

        if (viewMode == ItemsViewMode.SHOW_ALL_ITEMS) {
            showItemsList(items);
        } else {
            showGroupedItems(items);
        }
    }

    private void showItemsList(List<PantryItemWithDetails> items) {
        Log.d(TAG, "showItemsList called with " + (items != null ? items.size() : "null") + " items");

        if (items == null || items.isEmpty()) {
            showEmptyState();
        } else {
            // Switch to single items adapter
            binding.recyclerViewItems.setAdapter(itemsAdapter);

            binding.recyclerViewItems.setVisibility(View.VISIBLE);
            binding.wipContainer.setVisibility(View.GONE);
            binding.emptyStateContainer.setVisibility(View.GONE);
            binding.fabAddItem.setVisibility(View.VISIBLE);

            itemsAdapter.setItems(items);
            Log.d(TAG, "Items list shown successfully with " + items.size() + " items");
        }
    }

    private void showGroupedItems(List<PantryItemWithDetails> items) {
        Log.d(TAG, "showGroupedItems called with " + (items != null ? items.size() : "null") + " items");

        if (items == null || items.isEmpty()) {
            showEmptyState();
        } else {
            // Switch to grouped adapter
            binding.recyclerViewItems.setAdapter(groupedItemsAdapter);

            binding.recyclerViewItems.setVisibility(View.VISIBLE);
            binding.wipContainer.setVisibility(View.GONE);
            binding.emptyStateContainer.setVisibility(View.GONE);
            binding.fabAddItem.setVisibility(View.VISIBLE);

            // Group items and set to adapter
            var groupedItems = GroupingHelper.groupItemsByPantry(items);
            groupedItemsAdapter.setGroupedItems(groupedItems);
            Log.d(TAG, "Grouped items shown successfully with " + groupedItems.size() + " total entries");
        }
    }

    private void showEmptyState() {
        Log.d(TAG, "showEmptyState called");
        binding.recyclerViewItems.setVisibility(View.GONE);
        binding.wipContainer.setVisibility(View.GONE);
        binding.fabAddItem.setVisibility(View.VISIBLE);
        binding.emptyStateContainer.setVisibility(View.VISIBLE);
    }

    private void updateChipSelection(ItemsViewMode viewMode) {
        int chipId = viewMode == ItemsViewMode.GROUP_BY_PANTRY ?
                R.id.chip_group_by_pantry : R.id.chip_show_all_items;

        Chip targetChip = binding.getRoot().findViewById(chipId);
        if (targetChip != null && !targetChip.isChecked()) {
            Log.d(TAG, "Updating chip selection to: " + chipId);
            binding.filterChipGroup.setOnCheckedStateChangeListener(null);
            binding.filterChipGroup.check(chipId);
            setupFilterChips();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Inner class to handle clicks from both adapters
    private class ItemClickHandler implements ItemsAdapter.OnItemClickListener, GroupedItemsAdapter.OnItemClickListener {

        @Override
        public void onItemClick(PantryItemWithDetails item) {
            Log.d(TAG, "Item clicked: " + item.productName);
            // WIP - Show item details or edit dialog
        }

        @Override
        public void onItemLongClick(PantryItemWithDetails item) {
            Log.d(TAG, "Item long clicked: " + item.productName);
            // WIP - Show context menu or quick actions
        }

        @Override
        public void onHeaderClick(String pantryName, boolean isExpanded) {
            Log.d(TAG, "Header clicked: " + pantryName + ", expanded: " + isExpanded);
            // Find the current adapter and toggle if it's the grouped one
            if (binding.recyclerViewItems.getAdapter() instanceof GroupedItemsAdapter) {
                // For now we'll just log - the expand/collapse logic can be enhanced later
                // You could store header positions and call groupedItemsAdapter.toggleGroup(position)
            }
        }
    }
}