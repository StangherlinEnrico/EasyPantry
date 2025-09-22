package com.stanga.easypantry.ui.pantry_items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import com.stanga.easypantry.databinding.FragmentItemBinding;
import com.stanga.easypantry.enums.ItemsViewMode;

public class ItemFragment extends Fragment implements OnPantryItemClickListener {
    private FragmentItemBinding binding;
    private PantryItemViewModel viewModel;
    private PantryItemAdapter adapter;
    private PantryGroupAdapter groupAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PantryItemViewModel.class);

        setupRecyclerView();
        setupSearch();
        setupObservers();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new PantryItemAdapter(this); // PASSA THIS
        groupAdapter = new PantryGroupAdapter();
        groupAdapter.setOnItemClickListener(this); // IMPOSTA LISTENER
        binding.recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onPantryItemClick(PantryItemWithDetails item) {
        ItemManageDialogFragment.newInstance(item)
                .show(getChildFragmentManager(), "ManageItemDialog");
    }

    private void setupSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupObservers() {
        ItemsViewMode viewMode = viewModel.getCurrentViewMode();

        if (viewMode == ItemsViewMode.GROUPED_BY_PANTRY) {
            binding.recyclerViewItems.setAdapter(groupAdapter);
            viewModel.getGroupedSearchResults().observe(getViewLifecycleOwner(), groups -> {
                groupAdapter.setGroupedItems(groups);
                updateEmptyState(groups == null || groups.isEmpty());
            });
        } else {
            binding.recyclerViewItems.setAdapter(adapter);
            viewModel.getSearchResults().observe(getViewLifecycleOwner(), items -> {
                adapter.setItems(items);
                updateEmptyState(items == null || items.isEmpty());
            });
        }
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            binding.emptyStateContainer.setVisibility(View.VISIBLE);
            binding.recyclerViewItems.setVisibility(View.GONE);

            // Update empty state text based on search
            String query = binding.searchView.getQuery().toString();
            if (!query.trim().isEmpty()) {
                binding.emptyTitle.setText(R.string.empty_search_results);
                binding.emptyDescription.setText(R.string.empty_search_suggestion);
            } else {
                binding.emptyTitle.setText(R.string.items_empty_title);
                binding.emptyDescription.setText(R.string.items_empty_description);
            }
        } else {
            binding.emptyStateContainer.setVisibility(View.GONE);
            binding.recyclerViewItems.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}