package com.stanga.easypantry.ui.items;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Pantry;
import com.stanga.easypantry.database.entities.PantryItem;
import com.stanga.easypantry.database.entities.Product;
import com.stanga.easypantry.ui.pantry.PantryViewModel;
import com.stanga.easypantry.ui.product.ProductViewModel;

public class PantryItemDialogFragment extends DialogFragment {

    private View productSelector, pantrySelector;
    private TextView textSelectedProduct, textSelectedPantry;
    private TextInputEditText editTextQuantity;
    private AutoCompleteTextView dropdownUnit;
    private TextInputLayout textInputQuantity, textInputUnit;
    private MaterialButton btnCancel, btnAdd;

    private Product selectedProduct;
    private Pantry selectedPantry;

    private ItemsViewModel itemsViewModel;
    private ProductViewModel productViewModel;
    private PantryViewModel pantryViewModel;

    public static PantryItemDialogFragment newInstance() {
        return new PantryItemDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        itemsViewModel = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        pantryViewModel = new ViewModelProvider(requireActivity()).get(PantryViewModel.class);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pantry_item, null);
        initViews(view);
        setupUnitDropdown();
        setupTextWatchers();
        setupClickListeners();

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = (int) (displayMetrics.widthPixels * 0.9);

            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return dialog;
    }

    private void initViews(View view) {
        productSelector = view.findViewById(R.id.product_selector);
        pantrySelector = view.findViewById(R.id.pantry_selector);
        textSelectedProduct = view.findViewById(R.id.text_selected_product);
        textSelectedPantry = view.findViewById(R.id.text_selected_pantry);
        editTextQuantity = view.findViewById(R.id.edit_text_quantity);
        dropdownUnit = view.findViewById(R.id.dropdown_unit);
        textInputQuantity = view.findViewById(R.id.text_input_quantity);
        textInputUnit = view.findViewById(R.id.text_input_unit);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnAdd = view.findViewById(R.id.btn_add);
    }

    private void setupUnitDropdown() {
        String[] units = getResources().getStringArray(R.array.flour_yeast_units);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                units
        );
        dropdownUnit.setAdapter(adapter);

        // Set default value to kg (most common for flour)
        dropdownUnit.setText(getString(R.string.unit_kg), false);
    }

    private void setupTextWatchers() {
        TextWatcher validationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateAddButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editTextQuantity.addTextChangedListener(validationWatcher);
        dropdownUnit.addTextChangedListener(validationWatcher);
    }

    private void setupClickListeners() {
        productSelector.setOnClickListener(v -> showProductSelector());
        pantrySelector.setOnClickListener(v -> showPantrySelector());
        btnCancel.setOnClickListener(v -> dismiss());
        btnAdd.setOnClickListener(v -> addItem());
    }

    private void showProductSelector() {
        ProductSelectorDialogFragment.newInstance(selectedProduct)
                .setOnProductSelectedListener(product -> {
                    selectedProduct = product;
                    updateProductDisplay();
                    updateAddButtonState();
                    suggestUnit(product);
                })
                .show(getChildFragmentManager(), "ProductSelector");
    }

    private void showPantrySelector() {
        PantrySelectorDialogFragment.newInstance(selectedPantry)
                .setOnPantrySelectedListener(pantry -> {
                    selectedPantry = pantry;
                    updatePantryDisplay();
                    updateAddButtonState();
                })
                .show(getChildFragmentManager(), "PantrySelector");
    }

    private void suggestUnit(Product product) {
        if (product == null || product.name == null) return;

        String productName = product.name.toLowerCase();

        // Smart unit suggestion based on Italian product names
        if (productName.contains("lievito")) {
            if (productName.contains("cubetto") || productName.contains("cubo")) {
                dropdownUnit.setText(getString(R.string.unit_cubetto), false);
            } else if (productName.contains("secco") || productName.contains("istantaneo")) {
                if (productName.contains("bustina")) {
                    dropdownUnit.setText(getString(R.string.unit_bustina), false);
                } else {
                    dropdownUnit.setText(getString(R.string.unit_g), false);
                }
            } else if (productName.contains("fresco")) {
                dropdownUnit.setText(getString(R.string.unit_g), false);
            } else if (productName.contains("madre") || productName.contains("pasta")) {
                dropdownUnit.setText(getString(R.string.unit_g), false);
            } else {
                dropdownUnit.setText(getString(R.string.unit_confezione), false);
            }
        } else if (productName.contains("farina")) {
            if (productName.contains("sacco") || productName.contains("25kg") || productName.contains("10kg")) {
                dropdownUnit.setText(getString(R.string.unit_sacco), false);
            } else if (productName.contains("scatola")) {
                dropdownUnit.setText(getString(R.string.unit_scatola), false);
            } else {
                dropdownUnit.setText(getString(R.string.unit_kg), false);
            }
        } else if (productName.contains("semola") || productName.contains("semolino")) {
            dropdownUnit.setText(getString(R.string.unit_kg), false);
        } else if (productName.contains("amido") || productName.contains("fecola")) {
            if (productName.contains("bustina")) {
                dropdownUnit.setText(getString(R.string.unit_bustina), false);
            } else {
                dropdownUnit.setText(getString(R.string.unit_g), false);
            }
        } else if (productName.contains("glutine")) {
            dropdownUnit.setText(getString(R.string.unit_g), false);
        } else if (productName.contains("miglioratore") || productName.contains("additivo")) {
            dropdownUnit.setText(getString(R.string.unit_bustina), false);
        } else {
            // Keep current selection for other products
        }
    }

    private void updateProductDisplay() {
        if (textSelectedProduct != null) {
            if (selectedProduct != null) {
                String displayText = selectedProduct.name;
                if (selectedProduct.brand != null && !selectedProduct.brand.trim().isEmpty()) {
                    displayText += " - " + selectedProduct.brand;
                }
                textSelectedProduct.setText(displayText);
            } else {
                textSelectedProduct.setText(getString(R.string.item_no_product_selected));
            }
        }
    }

    private void updatePantryDisplay() {
        if (textSelectedPantry != null) {
            if (selectedPantry != null) {
                textSelectedPantry.setText(selectedPantry.name);
            } else {
                textSelectedPantry.setText(getString(R.string.item_no_pantry_selected));
            }
        }
    }

    private void updateAddButtonState() {
        String quantity = editTextQuantity.getText().toString().trim();
        String unit = dropdownUnit.getText().toString().trim();

        boolean isValid = selectedProduct != null &&
                selectedPantry != null &&
                !quantity.isEmpty() &&
                !unit.isEmpty() &&
                isValidQuantity(quantity);

        btnAdd.setEnabled(isValid);
    }

    private boolean isValidQuantity(String quantityStr) {
        try {
            float quantity = Float.parseFloat(quantityStr);
            return quantity > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void addItem() {
        if (selectedProduct == null || selectedPantry == null) return;

        String quantityStr = editTextQuantity.getText().toString().trim();
        String unit = dropdownUnit.getText().toString().trim();

        if (quantityStr.isEmpty() || unit.isEmpty()) {
            return;
        }

        try {
            float quantityFloat = Float.parseFloat(quantityStr);
            if (quantityFloat <= 0) {
                textInputQuantity.setError(getString(R.string.item_quantity_invalid));
                return;
            }

            // Convert to int for storage (multiply by 1000 for decimal precision if needed)
            int quantity = Math.round(quantityFloat * 1000);

            PantryItem newItem = new PantryItem(
                    selectedProduct.id,
                    selectedPantry.id,
                    quantity,
                    unit
            );

            itemsViewModel.insertItem(newItem);
            dismiss();

        } catch (NumberFormatException e) {
            textInputQuantity.setError(getString(R.string.item_quantity_invalid));
        }
    }
}