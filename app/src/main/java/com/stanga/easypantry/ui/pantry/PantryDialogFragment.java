package com.stanga.easypantry.ui.pantry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Pantry;

public class PantryDialogFragment extends DialogFragment {
    private static final String ARG_PANTRY = "pantry";

    private TextInputEditText editTextName;
    private TextInputLayout textInputLayout;
    private MaterialButton btnCancel, btnSave;

    private Pantry pantry;
    private PantryViewModel pantryViewModel;
    private boolean isEditMode = false;

    public static PantryDialogFragment newInstance(@Nullable Pantry pantry) {
        PantryDialogFragment fragment = new PantryDialogFragment();
        Bundle args = new Bundle();
        if (pantry != null) {
            args.putSerializable(ARG_PANTRY, pantry);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        pantryViewModel = new ViewModelProvider(requireActivity()).get(PantryViewModel.class);

        if (getArguments() != null) {
            pantry = (Pantry) getArguments().getSerializable(ARG_PANTRY);
            isEditMode = pantry != null;
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pantry, null);
        initViews(view);
        setupTextWatcher();
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

        // Auto-focus and show keyboard
        editTextName.post(() -> {
            editTextName.requestFocus();
            if (!isEditMode) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(editTextName, InputMethodManager.SHOW_IMPLICIT);
                }
            } else {
                editTextName.setSelection(editTextName.getText().length());
            }
        });

        return dialog;
    }

    private void initViews(View view) {
        editTextName = view.findViewById(R.id.edit_text_name);
        textInputLayout = view.findViewById(R.id.text_input_layout);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSave = view.findViewById(R.id.btn_save);
    }

    private void setupTextWatcher() {
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSaveButtonState();
                clearErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        btnCancel.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> savePantry());
    }

    private void setupInitialData() {
        if (isEditMode && pantry != null) {
            editTextName.setText(pantry.name);
            btnSave.setText(getString(R.string.dialog_save));
        } else {
            btnSave.setText(getString(R.string.dialog_add));
        }
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        String currentName = editTextName.getText().toString().trim();
        boolean isValidName = !currentName.isEmpty();
        btnSave.setEnabled(isValidName);
    }

    private void clearErrors() {
        textInputLayout.setError(null);
    }

    private void savePantry() {
        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {
            textInputLayout.setError(getString(R.string.pantry_name_required));
            editTextName.requestFocus();
            return;
        }

        if (name.length() > 50) {
            textInputLayout.setError(getString(R.string.pantry_name_too_long));
            return;
        }

        if (isEditMode && pantry != null) {
            pantry.name = name;
            pantryViewModel.update(pantry);
        } else {
            Pantry newPantry = new Pantry(name);
            pantryViewModel.insert(newPantry);
        }

        dismiss();
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