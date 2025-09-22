package com.stanga.easypantry.ui.pantry_items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PantryGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private OnPantryItemClickListener listener;

    private List<Object> items = new ArrayList<>();
    private List<PantryGroup> groups = new ArrayList<>();
    private Map<String, Boolean> expandedState = new HashMap<>();

    public void setOnItemClickListener(OnPantryItemClickListener listener) {
        this.listener = listener;
    }

    public void setGroupedItems(List<PantryGroup> groups) {
        this.groups = groups;

        // Initialize all groups as expanded
        for (PantryGroup group : groups) {
            if (!expandedState.containsKey(group.pantryName)) {
                expandedState.put(group.pantryName, true);
            }
        }

        rebuildItemsList();
    }

    private void rebuildItemsList() {
        items.clear();
        for (PantryGroup group : groups) {
            items.add(group.pantryName);
            if (expandedState.get(group.pantryName)) {
                items.addAll(group.items);
            }
        }
        notifyDataSetChanged();
    }

    private void toggleGroup(String pantryName) {
        boolean currentState = expandedState.get(pantryName);
        expandedState.put(pantryName, !currentState);
        rebuildItemsList();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_pantry_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_pantry_item_grouped, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String pantryName = (String) items.get(position);
            ((HeaderViewHolder) holder).bind(pantryName, getItemCountForPantry(pantryName), expandedState.get(pantryName));
        } else {
            PantryItemWithDetails item = (PantryItemWithDetails) items.get(position);
            ((ItemViewHolder) holder).bind(item, listener); // PASSA IL LISTENER
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int getItemCountForPantry(String pantryName) {
        for (PantryGroup group : groups) {
            if (group.pantryName.equals(pantryName)) {
                return group.items.size();
            }
        }
        return 0;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView pantryName, itemCount;
        ImageView expandIcon;

        HeaderViewHolder(View view) {
            super(view);
            pantryName = view.findViewById(R.id.text_pantry_name);
            itemCount = view.findViewById(R.id.text_item_count);
            expandIcon = view.findViewById(R.id.icon_expand);

            view.setOnClickListener(v -> {
                String name = (String) items.get(getAdapterPosition());
                toggleGroup(name);
            });
        }

        void bind(String name, int count, boolean isExpanded) {
            pantryName.setText(name);
            String countText = itemView.getContext().getResources()
                    .getQuantityString(R.plurals.items_count, count, count);
            itemCount.setText(countText);

            expandIcon.setImageResource(isExpanded ? R.drawable.ic_expand_less : R.drawable.ic_chevron_right);
            expandIcon.setRotation(isExpanded ? 0 : 0);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView productName, brandName, quantity;

        ItemViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.text_product_name);
            brandName = view.findViewById(R.id.text_brand_name);
            quantity = view.findViewById(R.id.text_quantity);
        }

        void bind(PantryItemWithDetails item, OnPantryItemClickListener listener) { // MODIFICA PARAMETRI
            productName.setText(item.productName);

            if (item.productBrand != null && !item.productBrand.trim().isEmpty()) {
                brandName.setText(item.productBrand);
                brandName.setVisibility(View.VISIBLE);
            } else {
                brandName.setVisibility(View.GONE);
            }

            quantity.setText(item.quantity + "g");

            // AGGIUNGI CLICK LISTENER
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onPantryItemClick(item);
            });
        }
    }

    public static class PantryGroup {
        public String pantryName;
        public List<PantryItemWithDetails> items;

        public PantryGroup(String pantryName, List<PantryItemWithDetails> items) {
            this.pantryName = pantryName;
            this.items = items;
        }
    }
}