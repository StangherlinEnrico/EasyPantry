package com.stanga.easypantry.ui.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Product;

public class ProductManageDialogFragment extends DialogFragment {
    private static final String ARG_PRODUCT = "product";

    private TextInputEditText editTextName, editTextBrand;
    private TextInputLayout textInputName, textInputBrand;
    private View btnDeleteProduct;
    private MaterialButton btnCancel, btnSave;

    private Product product;
    private ProductViewModel productViewModel;
    private String originalName, originalBrand;

    public static ProductManageDialogFragment newInstance(Product product) {
        ProductManageDialogFragment fragment = new ProductManageDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
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

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product_manage, null);
        initViews(view);
        setupTextWatchers();
        setupClickListeners();
        setupInitialData();

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = (int) (displayMetrics.widthPixels * 0.9);

            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        editTextName.post(() -> {
            editTextName.requestFocus();
            editTextName.setSelection(editTextName.getText().length());
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editTextName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        return dialog;
    }

    private void initViews(View view) {
        editTextName = view.findViewById(R.id.edit_text_name);
        editTextBrand = view.findViewById(R.id.edit_text_brand);
        textInputName = view.findViewById(R.id.text_input_name);
        textInputBrand = view.findViewById(R.id.text_input_brand);
        btnDeleteProduct = view.findViewById(R.id.btn_delete_product);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSave = view.findViewById(R.id.btn_save);
    }

    private void setupTextWatchers() {
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState();
                textInputName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editTextBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState();
                textInputBrand.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        btnDeleteProduct.setOnClickListener(v -> showDeleteConfirmation());
        btnCancel.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void setupInitialData() {
        if (product != null) {
            originalName = product.name;
            originalBrand = product.brand;
            editTextName.setText(product.name);
            editTextBrand.setText(product.brand);
        }
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        String currentName = editTextName.getText().toString().trim();
        String currentBrand = editTextBrand.getText().toString().trim();

        boolean hasNameChanged = !currentName.equals(originalName);
        boolean hasBrandChanged = !currentBrand.equals(originalBrand == null ? "" : originalBrand);
        boolean hasChanges = hasNameChanged || hasBrandChanged;
        boolean isValidName = !currentName.isEmpty();

        btnSave.setEnabled(hasChanges && isValidName);
    }

    private void saveProduct() {
        String name = editTextName.getText().toString().trim();
        String brand = editTextBrand.getText().toString().trim();

        if (name.isEmpty()) {
            textInputName.setError(getString(R.string.product_name_required));
            editTextName.requestFocus();
            return;
        }

        if (name.length() > 100) {
            textInputName.setError(getString(R.string.product_name_too_long));
            return;
        }

        if (!brand.isEmpty() && brand.length() > 50) {
            textInputBrand.setError(getString(R.string.product_brand_too_long));
            return;
        }

        if (product != null) {
            product.name = name;
            product.brand = brand.isEmpty() ? null : brand;
            productViewModel.update(product);
        }

        dismiss();
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.product_delete_confirm_title))
                .setMessage(getString(R.string.product_delete_confirm_message, product.name))
                .setPositiveButton(getString(R.string.dialog_delete), (dialog, which) -> {
                    productViewModel.delete(product);
                    dismiss();
                })
                .setNegativeButton(getString(R.string.dialog_cancel), null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getView() != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
}