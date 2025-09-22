package com.stanga.easypantry.ui.pantry_items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import java.util.ArrayList;
import java.util.List;

public class PantryItemAdapter extends RecyclerView.Adapter<PantryItemAdapter.ItemViewHolder> {
    private List<PantryItemWithDetails> items = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        PantryItemWithDetails item = items.get(position);
        holder.bind(item);
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
        TextView productName, brandName, pantryName, quantity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.text_product_name);
            brandName = itemView.findViewById(R.id.text_brand_name);
            pantryName = itemView.findViewById(R.id.text_pantry_name);
            quantity = itemView.findViewById(R.id.text_quantity);
        }

        public void bind(PantryItemWithDetails item) {
            productName.setText(item.productName);

            if (item.productBrand != null && !item.productBrand.trim().isEmpty()) {
                brandName.setText(item.productBrand);
                brandName.setVisibility(View.VISIBLE);
            } else {
                brandName.setVisibility(View.GONE);
            }

            pantryName.setText(item.pantryName);
            quantity.setText(String.valueOf(item.quantity) + "g");
        }
    }
}