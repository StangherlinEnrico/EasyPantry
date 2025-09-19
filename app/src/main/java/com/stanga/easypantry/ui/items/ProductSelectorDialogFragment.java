package com.stanga.easypantry.ui.items;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Product;
import com.stanga.easypantry.ui.product.OnProductClickListener;
import com.stanga.easypantry.ui.product.ProductAdapter;
import com.stanga.easypantry.ui.product.ProductViewModel;

public class ProductSelectorDialogFragment extends DialogFragment implements OnProductClickListener {

    private static final String ARG_SELECTED_PRODUCT = "selected_product";

    private SearchView searchView;
    private RecyclerView recyclerView;
    private MaterialButton btnCancel;
    private ProductAdapter adapter;
    private ProductViewModel productViewModel;
    private Product selectedProduct;
    private OnProductSelectedListener listener;

    public interface OnProductSelectedListener {
        void onProductSelected(Product product);
    }

    public static ProductSelectorDialogFragment newInstance(@Nullable Product selectedProduct) {
        ProductSelectorDialogFragment fragment = new ProductSelectorDialogFragment();
        Bundle args = new Bundle();
        if (selectedProduct != null) {
            args.putSerializable(ARG_SELECTED_PRODUCT, selectedProduct);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public ProductSelectorDialogFragment setOnProductSelectedListener(OnProductSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        if (getArguments() != null) {
            selectedProduct = (Product) getArguments().getSerializable(ARG_SELECTED_PRODUCT);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product_selector, null);
        initViews(view);
        setupRecyclerView();
        setupSearch();
        setupObservers();

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.7);
            dialog.getWindow().setLayout(width, height);
        }

        return dialog;
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.recycler_view_products);
        btnCancel = view.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        productViewModel.setSearchQuery("");
        productViewModel.getSearchResults().observe(this, products -> {
            if (products != null) {
                adapter.setProducts(products);
            }
        });
    }

    @Override
    public void onProductClick(Product product) {
        if (listener != null) {
            listener.onProductSelected(product);
        }
        dismiss();
    }

    @Override
    public void onProductLongClick(Product product) {
        // Not used in selector
    }
}