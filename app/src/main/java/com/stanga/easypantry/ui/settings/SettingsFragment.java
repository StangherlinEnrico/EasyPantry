package com.stanga.easypantry.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.stanga.easypantry.MainActivity;
import com.stanga.easypantry.databinding.FragmentSettingsBinding;
import com.stanga.easypantry.enums.ItemsViewMode;
import com.stanga.easypantry.enums.LanguageMode;
import com.stanga.easypantry.enums.ThemeMode;
import com.stanga.easypantry.utils.ItemsViewHelper;
import com.stanga.easypantry.utils.LanguageHelper;
import com.stanga.easypantry.utils.ThemeHelper;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupThemeSelector();
        setupLanguageSelector();
        setupItemsViewSelector(); // AGGIUNGI QUESTA RIGA
        updateThemeText();
        updateLanguageText();
        updateItemsViewText(); // AGGIUNGI QUESTA RIGA

        return root;
    }

    private void setupItemsViewSelector() {
        binding.itemsViewSelector.setOnClickListener(v -> showCustomItemsViewDialog());
    }

    private void showCustomItemsViewDialog() {
        ItemsViewMode currentViewMode = ItemsViewHelper.getSavedItemsViewMode(requireContext());

        CustomItemsViewDialog dialog = new CustomItemsViewDialog(requireContext(), currentViewMode, selectedViewMode -> {
            ItemsViewHelper.saveItemsViewMode(requireContext(), selectedViewMode);
            updateItemsViewText();
        });

        dialog.show();
    }

    private void updateItemsViewText() {
        ItemsViewMode currentViewMode = ItemsViewHelper.getSavedItemsViewMode(requireContext());
        binding.itemsViewValue.setText(currentViewMode.getDisplayName(requireContext()));
    }

    private void setupThemeSelector() {
        binding.themeSelector.setOnClickListener(v -> showCustomThemeDialog());
    }

    private void setupLanguageSelector() {
        binding.languageSelector.setOnClickListener(v -> showCustomLanguageDialog());
    }

    private void showCustomThemeDialog() {
        ThemeMode currentTheme = ThemeHelper.getSavedTheme(requireContext());

        CustomThemeDialog dialog = new CustomThemeDialog(requireContext(), currentTheme, selectedTheme -> {
            ThemeHelper.saveTheme(requireContext(), selectedTheme);
            ThemeHelper.applyTheme(selectedTheme);
            updateThemeText();
        });

        dialog.show();
    }

    private void showCustomLanguageDialog() {
        LanguageMode currentLanguage = LanguageHelper.getSavedLanguage(requireContext());

        CustomLanguageDialog dialog = new CustomLanguageDialog(requireContext(), currentLanguage, selectedLanguage -> {
            LanguageHelper.saveLanguage(requireContext(), selectedLanguage);

            // Restart activity to apply language change
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        dialog.show();
    }

    private void updateThemeText() {
        ThemeMode currentTheme = ThemeHelper.getSavedTheme(requireContext());
        binding.themeValue.setText(currentTheme.getDisplayName(requireContext()));
    }

    private void updateLanguageText() {
        LanguageMode currentLanguage = LanguageHelper.getSavedLanguage(requireContext());
        binding.languageValue.setText(currentLanguage.getDisplayName(requireContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}