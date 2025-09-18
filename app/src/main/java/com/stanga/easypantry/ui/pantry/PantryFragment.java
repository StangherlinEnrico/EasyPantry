package com.stanga.easypantry.ui.pantry;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.stanga.easypantry.databinding.FragmentPantryBinding;
import com.stanga.easypantry.database.entities.Pantry;

public class PantryFragment extends Fragment implements OnPantryClickListener {

    private FragmentPantryBinding binding;
    private PantryAdapter pantryAdapter;
    private PantryViewModel pantryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pantryViewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        binding = FragmentPantryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        setupFab();

        pantryViewModel.getAllPantries().observe(getViewLifecycleOwner(), pantries -> {
            pantryAdapter.setPantries(pantries);
        });

        return root;
    }

    private void setupRecyclerView() {
        pantryAdapter = new PantryAdapter(this);
        binding.recyclerViewPantries.setAdapter(pantryAdapter);
        binding.recyclerViewPantries.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupFab() {
        binding.fabAddPantry.setOnClickListener(v -> showAddPantryDialog());
    }

    private void showAddPantryDialog() {
        PantryDialogFragment.newInstance(null)
                .show(getChildFragmentManager(), "AddPantryDialog");
    }

    private void showEditPantryDialog(Pantry pantry) {
        PantryDialogFragment.newInstance(pantry)
                .show(getChildFragmentManager(), "EditPantryDialog");
    }

    private void showDeleteConfirmDialog(Pantry pantry) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Pantry")
                .setMessage("Are you sure you want to delete \"" + pantry.name + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> pantryViewModel.delete(pantry))
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onPantryClick(Pantry pantry) {
        showEditPantryDialog(pantry);
    }

    @Override
    public void onPantryLongClick(Pantry pantry) {
        showDeleteConfirmDialog(pantry);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}