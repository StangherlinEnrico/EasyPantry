package com.stanga.easypantry.ui.pantry_items;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ItemManageDialogFragment extends DialogFragment {
    private static final String ARG_ITEM = "item";

    private TextView textProductName, textProductBrand, textExpiryDate, textInitialQuantity, textPantryName;
    private View expirySection, btnDeleteItem;
    private TextInputEditText editTextQuantity;
    private MaterialButton btnMinus1000, btnMinus500, btnMinus100;
    private MaterialButton btnPlus100, btnPlus500, btnPlus1000;
    private MaterialButton btnCancel, btnSave;

    private PantryItemWithDetails item;
    private PantryItemViewModel viewModel;
    private int currentQuantity;
    private int initialQuantity;

    public static ItemManageDialogFragment newInstance(PantryItemWithDetails item) {
        ItemManageDialogFragment fragment = new ItemManageDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(PantryItemViewModel.class);

        if (getArguments() != null) {
            item = (PantryItemWithDetails) getArguments().getSerializable(ARG_ITEM);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_item_manage, null);
        initViews(view);
        setupInitialData();
        setupClickListeners();
        setupTextWatcher();

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
        textProductName = view.findViewById(R.id.text_product_name);
        textProductBrand = view.findViewById(R.id.text_product_brand);
        textPantryName = view.findViewById(R.id.text_pantry_name);
        textExpiryDate = view.findViewById(R.id.text_expiry_date);
        textInitialQuantity = view.findViewById(R.id.text_initial_quantity);
        expirySection = view.findViewById(R.id.expiry_section);

        editTextQuantity = view.findViewById(R.id.edit_text_quantity);

        btnMinus1000 = view.findViewById(R.id.btn_minus_1000);
        btnMinus500 = view.findViewById(R.id.btn_minus_500);
        btnMinus100 = view.findViewById(R.id.btn_minus_100);
        btnPlus100 = view.findViewById(R.id.btn_plus_100);
        btnPlus500 = view.findViewById(R.id.btn_plus_500);
        btnPlus1000 = view.findViewById(R.id.btn_plus_1000);

        btnDeleteItem = view.findViewById(R.id.btn_delete_item);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSave = view.findViewById(R.id.btn_save);
    }

    private void setupInitialData() {
        if (item == null) return;

        textProductName.setText(item.productName);

        if (item.productBrand != null && !item.productBrand.trim().isEmpty()) {
            textProductBrand.setText(item.productBrand);
            textProductBrand.setVisibility(View.VISIBLE);
        }

        textPantryName.setText(item.pantryName);

        if (item.expiryDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            textExpiryDate.setText(sdf.format(item.expiryDate));
            expirySection.setVisibility(View.VISIBLE);
        }

        initialQuantity = item.quantity;
        currentQuantity = item.quantity;
        textInitialQuantity.setText(initialQuantity + "g");
        editTextQuantity.setText(String.valueOf(currentQuantity));

        updateSaveButtonState();
    }

    private void setupClickListeners() {
        btnMinus1000.setOnClickListener(v -> adjustQuantity(-1000));
        btnMinus500.setOnClickListener(v -> adjustQuantity(-500));
        btnMinus100.setOnClickListener(v -> adjustQuantity(-100));

        btnPlus100.setOnClickListener(v -> adjustQuantity(100));
        btnPlus500.setOnClickListener(v -> adjustQuantity(500));
        btnPlus1000.setOnClickListener(v -> adjustQuantity(1000));

        btnDeleteItem.setOnClickListener(v -> showDeleteConfirmation());
        btnCancel.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void setupTextWatcher() {
        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().trim().isEmpty()) {
                        currentQuantity = Integer.parseInt(s.toString().trim());
                    }
                } catch (NumberFormatException e) {
                    currentQuantity = 0;
                }
                updateSaveButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void adjustQuantity(int delta) {
        currentQuantity = Math.max(0, currentQuantity + delta);
        editTextQuantity.setText(String.valueOf(currentQuantity));
    }

    private void setQuantity(int quantity) {
        currentQuantity = Math.max(0, quantity);
        editTextQuantity.setText(String.valueOf(currentQuantity));
    }

    private void updateSaveButtonState() {
        boolean hasChanges = currentQuantity != initialQuantity;
        boolean isValidQuantity = currentQuantity >= 0;
        btnSave.setEnabled(hasChanges && isValidQuantity);

        // Aggiorna il testo del pulsante in base alla quantità
        if (currentQuantity == 0) {
            btnSave.setText("Rimuovi");
        } else {
            btnSave.setText("Salva");
        }
    }

    private void saveChanges() {
        if (item == null || currentQuantity < 0) return;

        if (currentQuantity == 0) {
            // Se quantità è 0, rimuovi completamente l'item
            viewModel.deletePantryItem(item);
        } else {
            // Altrimenti aggiorna solo la quantità
            viewModel.updatePantryItemQuantity(item.id, currentQuantity);
        }

        dismiss();
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Rimuovi articolo")
                .setMessage("Sei sicuro di voler rimuovere \"" + item.productName + "\" dalla dispensa?")
                .setPositiveButton("Rimuovi", (dialog, which) -> {
                    viewModel.deletePantryItem(item); // RIMUOVI TODO
                    dismiss();
                })
                .setNegativeButton("Annulla", null)
                .show();
    }
}