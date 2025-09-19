package com.stanga.easypantry.ui.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<PantryItemWithDetails> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PantryItemWithDetails item);
        void onItemLongClick(PantryItemWithDetails item);
    }

    public ItemsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry_item_single, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PantryItemWithDetails currentItem = items.get(position);
        holder.bind(currentItem, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<PantryItemWithDetails> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private TextView textViewBrand;
        private TextView textViewPantryName;
        private TextView textViewQuantity;
        private TextView textViewUnit;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.text_view_product_name);
            textViewBrand = itemView.findViewById(R.id.text_view_brand);
            textViewPantryName = itemView.findViewById(R.id.text_view_pantry_name);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
            textViewUnit = itemView.findViewById(R.id.text_view_unit);
        }

        public void bind(PantryItemWithDetails item, OnItemClickListener listener) {
            // Product name
            textViewProductName.setText(item.productName);

            // Brand (show only if available)
            if (item.productBrand != null && !item.productBrand.trim().isEmpty()) {
                textViewBrand.setText(item.productBrand);
                textViewBrand.setVisibility(View.VISIBLE);
            } else {
                textViewBrand.setVisibility(View.GONE);
            }

            // Pantry name
            textViewPantryName.setText(item.pantryName);

            // Quantity and unit
            float displayQuantity = item.quantity / 1000.0f;
            if (displayQuantity == Math.floor(displayQuantity)) {
                textViewQuantity.setText(String.valueOf(Math.round(displayQuantity)));
            } else {
                textViewQuantity.setText(String.format("%.1f", displayQuantity));
            }
            textViewUnit.setText(item.unit);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(item);
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onItemLongClick(item);
                return true;
            });
        }
    }
}