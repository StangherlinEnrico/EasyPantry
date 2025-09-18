package com.stanga.easypantry.ui.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Product;

public class ProductDialogFragment extends DialogFragment {
    private static final String ARG_PRODUCT = "product";
    private EditText editTextName;
    private Product product;
    private ProductViewModel productViewModel;

    public static ProductDialogFragment newInstance(@Nullable Product product) {
        ProductDialogFragment fragment = new ProductDialogFragment();
        Bundle args = new Bundle();
        if (product != null) {
            args.putSerializable(ARG_PRODUCT, product);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PRODUCT);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product, null);
        editTextName = view.findViewById(R.id.edit_text_name);

        if (product != null) {
            editTextName.setText(product.name);
        }

        String title = product == null ? "Add Product" : "Edit Product";
        String positiveButtonText = product == null ? "Add" : "Update";

        return new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveButtonText, (dialog, which) -> saveProduct())
                .setNegativeButton("Cancel", null)
                .create();
    }

    private void saveProduct() {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) return;

        if (product == null) {
            Product newProduct = new Product(name);
            productViewModel.insert(newProduct);
        } else {
            product.name = name;
            productViewModel.update(product);
        }
    }
}