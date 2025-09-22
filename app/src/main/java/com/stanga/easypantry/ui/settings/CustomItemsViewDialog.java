package com.stanga.easypantry.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.stanga.easypantry.R;
import com.stanga.easypantry.enums.ItemsViewMode;

public class CustomItemsViewDialog extends Dialog {

    private ItemsViewMode currentViewMode;
    private ItemsViewMode selectedViewMode;
    private OnItemsViewSelectedListener listener;

    private RadioButton radioGrouped, radioShowAll;
    private LinearLayout viewGrouped, viewShowAll;
    private MaterialButton btnCancel, btnApply;

    public interface OnItemsViewSelectedListener {
        void onItemsViewSelected(ItemsViewMode viewMode);
    }

    public CustomItemsViewDialog(@NonNull Context context, ItemsViewMode currentViewMode, OnItemsViewSelectedListener listener) {
        super(context);
        this.currentViewMode = currentViewMode;
        this.selectedViewMode = currentViewMode;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_items_view_selector, null);
        setContentView(view);

        // Make dialog background transparent
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            // AGGIUNGI QUESTE RIGHE per il dimensionamento
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                getContext().getDisplay().getRealMetrics(displayMetrics);
            } else {
                ((android.view.WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay().getMetrics(displayMetrics);
            }
            int width = (int) (displayMetrics.widthPixels * 0.9);
            getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        initViews(view);
        setupClickListeners();
        updateSelection();
    }

    private void initViews(View view) {
        viewGrouped = view.findViewById(R.id.items_view_grouped_option);
        viewShowAll = view.findViewById(R.id.items_view_show_all_option);

        radioGrouped = view.findViewById(R.id.radio_grouped);
        radioShowAll = view.findViewById(R.id.radio_show_all);

        btnCancel = view.findViewById(R.id.btn_cancel_items_view);
        btnApply = view.findViewById(R.id.btn_apply_items_view);
    }

    private void setupClickListeners() {
        viewGrouped.setOnClickListener(v -> selectViewMode(ItemsViewMode.GROUPED_BY_PANTRY));
        viewShowAll.setOnClickListener(v -> selectViewMode(ItemsViewMode.SHOW_ALL));

        btnCancel.setOnClickListener(v -> dismiss());
        btnApply.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemsViewSelected(selectedViewMode);
            }
            dismiss();
        });
    }

    private void selectViewMode(ItemsViewMode viewMode) {
        selectedViewMode = viewMode;
        updateSelection();
    }

    private void updateSelection() {
        radioGrouped.setChecked(selectedViewMode == ItemsViewMode.GROUPED_BY_PANTRY);
        radioShowAll.setChecked(selectedViewMode == ItemsViewMode.SHOW_ALL);
    }
}