package com.stanga.easypantry.ui.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.stanga.easypantry.R;
import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import com.stanga.easypantry.ui.items.models.GroupedItem;

public class GroupedItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GroupedItem> groupedItems = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PantryItemWithDetails item);
        void onItemLongClick(PantryItemWithDetails item);
        void onHeaderClick(String pantryName, boolean isExpanded);
    }

    public GroupedItemsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == GroupedItem.TYPE_HEADER) {
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
        GroupedItem item = groupedItems.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((GroupedItem.HeaderItem) item, listener);
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind(((GroupedItem.PantryItemWrapper) item).getItem(), listener);
        }
    }

    @Override
    public int getItemCount() {
        return groupedItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return groupedItems.get(position).getType();
    }

    public void setGroupedItems(List<GroupedItem> items) {
        this.groupedItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void toggleGroup(int position) {
        if (position < groupedItems.size() && groupedItems.get(position) instanceof GroupedItem.HeaderItem) {
            GroupedItem.HeaderItem header = (GroupedItem.HeaderItem) groupedItems.get(position);
            header.setExpanded(!header.isExpanded());

            // Find all items belonging to this header and toggle their visibility
            String pantryName = header.getPantryName();
            List<GroupedItem> newItems = new ArrayList<>();

            for (GroupedItem item : groupedItems) {
                if (item instanceof GroupedItem.HeaderItem) {
                    newItems.add(item);
                } else if (item instanceof GroupedItem.PantryItemWrapper) {
                    GroupedItem.PantryItemWrapper wrapper = (GroupedItem.PantryItemWrapper) item;
                    if (wrapper.getItem().pantryName.equals(pantryName)) {
                        if (header.isExpanded()) {
                            newItems.add(item);
                        }
                        // If not expanded, don't add the item (hide it)
                    } else {
                        newItems.add(item);
                    }
                }
            }

            setGroupedItems(newItems);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPantryName;
        private TextView textViewItemCount;
        private ImageView iconExpand;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textViewPantryName = itemView.findViewById(R.id.text_view_pantry_name);
            textViewItemCount = itemView.findViewById(R.id.text_view_item_count);
            iconExpand = itemView.findViewById(R.id.icon_expand);
        }

        public void bind(GroupedItem.HeaderItem header, OnItemClickListener listener) {
            textViewPantryName.setText(header.getPantryName());

            String countText = header.getItemCount() == 1 ?
                    header.getItemCount() + " item" :
                    header.getItemCount() + " items";
            textViewItemCount.setText(countText);

            // Update expand icon rotation
            iconExpand.setRotation(header.isExpanded() ? 0 : 180);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHeaderClick(header.getPantryName(), header.isExpanded());
                }
            });
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewProductName;
        private TextView textViewBrand;
        private TextView textViewQuantity;
        private TextView textViewUnit;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.text_view_product_name);
            textViewBrand = itemView.findViewById(R.id.text_view_brand);
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