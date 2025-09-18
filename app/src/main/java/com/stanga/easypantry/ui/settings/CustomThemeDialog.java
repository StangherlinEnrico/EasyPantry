package com.stanga.easypantry.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.stanga.easypantry.R;
import com.stanga.easypantry.enums.ThemeMode;

public class CustomThemeDialog extends Dialog {

    private ThemeMode currentTheme;
    private ThemeMode selectedTheme;
    private OnThemeSelectedListener listener;

    private RadioButton radioLight, radioDark, radioSystem;
    private LinearLayout themeLight, themeDark, themeSystem;
    private MaterialButton btnCancel, btnApply;

    public interface OnThemeSelectedListener {
        void onThemeSelected(ThemeMode themeMode);
    }

    public CustomThemeDialog(@NonNull Context context, ThemeMode currentTheme, OnThemeSelectedListener listener) {
        super(context);
        this.currentTheme = currentTheme;
        this.selectedTheme = currentTheme;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_theme_selector, null);
        setContentView(view);

        // Make dialog background transparent
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        initViews(view);
        setupClickListeners();
        updateSelection();
    }

    private void initViews(View view) {
        themeLight = view.findViewById(R.id.theme_light_option);
        themeDark = view.findViewById(R.id.theme_dark_option);
        themeSystem = view.findViewById(R.id.theme_system_option);

        radioLight = view.findViewById(R.id.radio_light);
        radioDark = view.findViewById(R.id.radio_dark);
        radioSystem = view.findViewById(R.id.radio_system);

        btnCancel = view.findViewById(R.id.btn_cancel);
        btnApply = view.findViewById(R.id.btn_apply);
    }

    private void setupClickListeners() {
        themeLight.setOnClickListener(v -> selectTheme(ThemeMode.LIGHT));
        themeDark.setOnClickListener(v -> selectTheme(ThemeMode.DARK));
        themeSystem.setOnClickListener(v -> selectTheme(ThemeMode.SYSTEM));

        btnCancel.setOnClickListener(v -> dismiss());
        btnApply.setOnClickListener(v -> {
            if (listener != null) {
                listener.onThemeSelected(selectedTheme);
            }
            dismiss();
        });
    }

    private void selectTheme(ThemeMode theme) {
        selectedTheme = theme;
        updateSelection();
    }

    private void updateSelection() {
        radioLight.setChecked(selectedTheme == ThemeMode.LIGHT);
        radioDark.setChecked(selectedTheme == ThemeMode.DARK);
        radioSystem.setChecked(selectedTheme == ThemeMode.SYSTEM);
    }
}