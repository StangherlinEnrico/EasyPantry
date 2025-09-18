package com.stanga.easypantry.ui.pantry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Pantry;

public class PantryManageDialogFragment extends DialogFragment {
    private static final String ARG_PANTRY = "pantry";

    private EditText editTextName;
    private LinearLayout btnEditPantry, btnDeletePantry;
    private MaterialButton btnCancel, btnSave;

    private Pantry pantry;
    private PantryViewModel pantryViewModel;
    private boolean isEditing = false;

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
        setupClickListeners();
        updateUI();

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
        editTextName = view.findViewById(R.id.edit_text_name);
        btnEditPantry = view.findViewById(R.id.btn_edit_pantry);
        btnDeletePantry = view.findViewById(R.id.btn_delete_pantry);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSave = view.findViewById(R.id.btn_save);
    }

    private void setupClickListeners() {
        btnEditPantry.setOnClickListener(v -> enableEditMode());

        btnDeletePantry.setOnClickListener(v -> showDeleteConfirmation());

        btnCancel.setOnClickListener(v -> {
            if (isEditing) {
                disableEditMode();
            } else {
                dismiss();
            }
        });

        btnSave.setOnClickListener(v -> savePantry());
    }

    private void updateUI() {
        if (pantry != null) {
            editTextName.setText(pantry.name);
            editTextName.setEnabled(false);
        }

        btnSave.setVisibility(View.GONE);
        btnCancel.setText(getString(R.string.dialog_close));
    }

    private void enableEditMode() {
        isEditing = true;
        editTextName.setEnabled(true);
        editTextName.requestFocus();
        editTextName.setSelection(editTextName.getText().length());

        btnEditPantry.setVisibility(View.GONE);
        btnDeletePantry.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setText(getString(R.string.dialog_cancel));
    }

    private void disableEditMode() {
        isEditing = false;
        editTextName.setEnabled(false);
        if (pantry != null) {
            editTextName.setText(pantry.name);
        }

        btnEditPantry.setVisibility(View.VISIBLE);
        btnDeletePantry.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        btnCancel.setText(getString(R.string.dialog_close));
    }

    private void savePantry() {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.pantry_name_required));
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
}