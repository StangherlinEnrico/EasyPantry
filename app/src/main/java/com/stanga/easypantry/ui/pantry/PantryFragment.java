package com.stanga.easypantry.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stanga.easypantry.databinding.FragmentPantryBinding;

public class PantryFragment extends Fragment {

    private FragmentPantryBinding binding;
    private PantryAdapter pantryAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PantryViewModel pantryViewModel =
                new ViewModelProvider(this).get(PantryViewModel.class);

        binding = FragmentPantryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();

        pantryViewModel.getAllPantries().observe(getViewLifecycleOwner(), pantries -> {
            pantryAdapter.setPantries(pantries);
        });

        return root;
    }

    private void setupRecyclerView() {
        pantryAdapter = new PantryAdapter();
        binding.recyclerViewPantries.setAdapter(pantryAdapter);
        binding.recyclerViewPantries.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}