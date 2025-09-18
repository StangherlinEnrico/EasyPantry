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
import com.stanga.easypantry.enums.LanguageMode;

public class CustomLanguageDialog extends Dialog {

    private LanguageMode currentLanguage;
    private LanguageMode selectedLanguage;
    private OnLanguageSelectedListener listener;

    private RadioButton radioSystemLang, radioEnglish, radioItalian;
    private LinearLayout languageSystem, languageEnglish, languageItalian;
    private MaterialButton btnCancelLang, btnApplyLang;

    public interface OnLanguageSelectedListener {
        void onLanguageSelected(LanguageMode languageMode);
    }

    public CustomLanguageDialog(@NonNull Context context, LanguageMode currentLanguage, OnLanguageSelectedListener listener) {
        super(context);
        this.currentLanguage = currentLanguage;
        this.selectedLanguage = currentLanguage;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_language_selector, null);
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
        languageSystem = view.findViewById(R.id.language_system_option);
        languageEnglish = view.findViewById(R.id.language_english_option);
        languageItalian = view.findViewById(R.id.language_italian_option);

        radioSystemLang = view.findViewById(R.id.radio_system_lang);
        radioEnglish = view.findViewById(R.id.radio_english);
        radioItalian = view.findViewById(R.id.radio_italian);

        btnCancelLang = view.findViewById(R.id.btn_cancel_lang);
        btnApplyLang = view.findViewById(R.id.btn_apply_lang);
    }

    private void setupClickListeners() {
        languageSystem.setOnClickListener(v -> selectLanguage(LanguageMode.SYSTEM));
        languageEnglish.setOnClickListener(v -> selectLanguage(LanguageMode.ENGLISH));
        languageItalian.setOnClickListener(v -> selectLanguage(LanguageMode.ITALIAN));

        btnCancelLang.setOnClickListener(v -> dismiss());
        btnApplyLang.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLanguageSelected(selectedLanguage);
            }
            dismiss();
        });
    }

    private void selectLanguage(LanguageMode language) {
        selectedLanguage = language;
        updateSelection();
    }

    private void updateSelection() {
        radioSystemLang.setChecked(selectedLanguage == LanguageMode.SYSTEM);
        radioEnglish.setChecked(selectedLanguage == LanguageMode.ENGLISH);
        radioItalian.setChecked(selectedLanguage == LanguageMode.ITALIAN);
    }
}