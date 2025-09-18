package com.stanga.easypantry.ui.pantry;

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
import com.stanga.easypantry.database.entities.Pantry;

public class PantryDialogFragment extends DialogFragment {
    private static final String ARG_PANTRY = "pantry";
    private EditText editTextName;
    private Pantry pantry;
    private PantryViewModel pantryViewModel;

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
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pantry, null);
        editTextName = view.findViewById(R.id.edit_text_name);

        if (pantry != null) {
            editTextName.setText(pantry.name);
        }

        String title = pantry == null ? "Add Pantry" : "Edit Pantry";
        String positiveButtonText = pantry == null ? "Add" : "Update";

        return new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveButtonText, (dialog, which) -> savePantry())
                .setNegativeButton("Cancel", null)
                .create();
    }

    private void savePantry() {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) return;

        if (pantry == null) {
            Pantry newPantry = new Pantry(name, "pantry");
            pantryViewModel.insert(newPantry);
        } else {
            pantry.name = name;
            pantryViewModel.update(pantry);
        }
    }
}