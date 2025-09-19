package com.stanga.easypantry.ui.items.models;

import com.stanga.easypantry.database.entities.PantryItemWithDetails;

public abstract class GroupedItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    public abstract int getType();

    public static class HeaderItem extends GroupedItem {
        private String pantryName;
        private int itemCount;
        private boolean expanded;

        public HeaderItem(String pantryName, int itemCount) {
            this.pantryName = pantryName;
            this.itemCount = itemCount;
            this.expanded = true; // Default expanded
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }

        public String getPantryName() {
            return pantryName;
        }

        public int getItemCount() {
            return itemCount;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }
    }

    public static class PantryItemWrapper extends GroupedItem {
        private PantryItemWithDetails item;

        public PantryItemWrapper(PantryItemWithDetails item) {
            this.item = item;
        }

        @Override
        public int getType() {
            return TYPE_ITEM;
        }

        public PantryItemWithDetails getItem() {
            return item;
        }
    }
}