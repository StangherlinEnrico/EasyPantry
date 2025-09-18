package com.stanga.easypantry.ui.product;

import android.app.AlertDialog;
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

        productViewModel.getSearchResults().observe(getViewLifecycleOwner(), products -> {
            productAdapter.setProducts(products);
        });

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

    private void showAddProductDialog() {
        ProductDialogFragment.newInstance(null)
                .show(getChildFragmentManager(), "AddProductDialog");
    }

    private void showEditProductDialog(Product product) {
        ProductDialogFragment.newInstance(product)
                .show(getChildFragmentManager(), "EditProductDialog");
    }

    private void showDeleteConfirmDialog(Product product) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete \"" + product.name + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> productViewModel.delete(product))
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onProductClick(Product product) {
        showEditProductDialog(product);
    }

    @Override
    public void onProductLongClick(Product product) {
        showDeleteConfirmDialog(product);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}