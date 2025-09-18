package com.stanga.easypantry.ui.pantry;

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
import com.stanga.easypantry.database.entities.Pantry;

public class PantryManageDialogFragment extends DialogFragment {
    private static final String ARG_PANTRY = "pantry";

    private TextInputEditText editTextName;
    private TextInputLayout textInputLayout;
    private View btnDeletePantry;
    private MaterialButton btnCancel, btnSave;

    private Pantry pantry;
    private PantryViewModel pantryViewModel;
    private String originalName;

    public static PantryManageDialogFragment newInstance(Pantry pantry) {
        PantryManageDialogFragment fragment = new PantryManageDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PANTRY, pantry);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        pantryViewModel = new ViewModelProvider(requireActivity()).get(PantryViewModel.class);

        if (getArguments() != null) {
            pantry = (Pantry) getArguments().getSerializable(ARG_PANTRY);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pantry_manage, null);
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
        textInputLayout = view.findViewById(R.id.text_input_layout);
        btnDeletePantry = view.findViewById(R.id.btn_delete_pantry);
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
        btnDeletePantry.setOnClickListener(v -> showDeleteConfirmation());
        btnCancel.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> savePantry());
    }

    private void setupInitialData() {
        if (pantry != null) {
            originalName = pantry.name;
            editTextName.setText(pantry.name);
        }
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        String currentName = editTextName.getText().toString().trim();
        boolean hasChanges = !currentName.equals(originalName);
        boolean isValidName = !currentName.isEmpty();

        btnSave.setEnabled(hasChanges && isValidName);
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

        if (pantry != null) {
            pantry.name = name;
            pantryViewModel.update(pantry);
        }

        dismiss();
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.pantry_delete_confirm_title))
                .setMessage(getString(R.string.pantry_delete_confirm_message, pantry.name))
                .setPositiveButton(getString(R.string.dialog_delete), (dialog, which) -> {
                    pantryViewModel.delete(pantry);
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