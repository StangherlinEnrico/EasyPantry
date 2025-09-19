package com.stanga.easypantry.ui.items;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.Pantry;
import com.stanga.easypantry.ui.pantry.OnPantryClickListener;
import com.stanga.easypantry.ui.pantry.PantryAdapter;
import com.stanga.easypantry.ui.pantry.PantryViewModel;

public class PantrySelectorDialogFragment extends DialogFragment implements OnPantryClickListener {

    private static final String ARG_SELECTED_PANTRY = "selected_pantry";

    private RecyclerView recyclerView;
    private MaterialButton btnCancel;
    private PantryAdapter adapter;
    private PantryViewModel pantryViewModel;
    private Pantry selectedPantry;
    private OnPantrySelectedListener listener;

    public interface OnPantrySelectedListener {
        void onPantrySelected(Pantry pantry);
    }

    public static PantrySelectorDialogFragment newInstance(@Nullable Pantry selectedPantry) {
        PantrySelectorDialogFragment fragment = new PantrySelectorDialogFragment();
        Bundle args = new Bundle();
        if (selectedPantry != null) {
            args.putSerializable(ARG_SELECTED_PANTRY, selectedPantry);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public PantrySelectorDialogFragment setOnPantrySelectedListener(OnPantrySelectedListener listener) {
        this.listener = listener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        pantryViewModel = new ViewModelProvider(requireActivity()).get(PantryViewModel.class);

        if (getArguments() != null) {
            selectedPantry = (Pantry) getArguments().getSerializable(ARG_SELECTED_PANTRY);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pantry_selector, null);
        initViews(view);
        setupRecyclerView();
        setupObservers();

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
            dialog.getWindow().setLayout(width, height);
        }

        return dialog;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_pantries);
        btnCancel = view.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void setupRecyclerView() {
        adapter = new PantryAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupObservers() {
        pantryViewModel.getAllPantries().observe(this, pantries -> {
            if (pantries != null) {
                adapter.setPantries(pantries);
            }
        });
    }

    @Override
    public void onPantryClick(Pantry pantry) {
        if (listener != null) {
            listener.onPantrySelected(pantry);
        }
        dismiss();
    }

    @Override
    public void onPantryLongClick(Pantry pantry) {
        // Not used in selector
    }
}