package com.stanga.easypantry.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.stanga.easypantry.databinding.FragmentProductBinding;
import com.stanga.easypantry.database.entities.Product;

import java.util.List;

public class ProductFragment extends Fragment implements OnProductClickListener {

    private FragmentProductBinding binding;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        setupFab();
        setupSearch();
        setupObservers();

        return root;
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this);
        binding.recyclerViewProducts.setAdapter(productAdapter);
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupFab() {
        binding.fabAddProduct.setOnClickListener(v -> showAddProductDialog());
    }

    private void setupSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productViewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productViewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupObservers() {
        // Initialize search query to trigger initial data load
        productViewModel.setSearchQuery("");

        productViewModel.getSearchResults().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                productAdapter.setProducts(products);
                updateEmptyState(products);
            } else {
                // Handle null case - show empty state
                productAdapter.setProducts(java.util.Collections.emptyList());
                updateEmptyState(java.util.Collections.emptyList());
            }
        });
    }

    private void showAddProductDialog() {
        ProductDialogFragment.newInstance(null)
                .show(getChildFragmentManager(), "AddProductDialog");
    }

    private void showManageProductDialog(Product product) {
        ProductManageDialogFragment.newInstance(product)
                .show(getChildFragmentManager(), "ManageProductDialog");
    }

    private void updateEmptyState(List<Product> products) {
        if (products == null || products.isEmpty()) {
            binding.emptyStateContainer.setVisibility(View.VISIBLE);
            binding.recyclerViewProducts.setVisibility(View.GONE);
        } else {
            binding.emptyStateContainer.setVisibility(View.GONE);
            binding.recyclerViewProducts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProductClick(Product product) {
        showManageProductDialog(product);
    }

    @Override
    public void onProductLongClick(Product product) {
        // Non utilizzato più, tutto gestito nel dialog di gestione
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}