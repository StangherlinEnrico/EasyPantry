package com.stanga.easypantry.ui.items.utils;

import com.stanga.easypantry.database.entities.PantryItemWithDetails;
import com.stanga.easypantry.ui.items.models.GroupedItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupingHelper {

    public static List<GroupedItem> groupItemsByPantry(List<PantryItemWithDetails> items) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }

        // Group items by pantry name
        Map<String, List<PantryItemWithDetails>> pantryGroups = new LinkedHashMap<>();

        for (PantryItemWithDetails item : items) {
            String pantryName = item.pantryName;
            pantryGroups.computeIfAbsent(pantryName, k -> new ArrayList<>()).add(item);
        }

        // Create grouped list with headers
        List<GroupedItem> groupedItems = new ArrayList<>();

        for (Map.Entry<String, List<PantryItemWithDetails>> entry : pantryGroups.entrySet()) {
            String pantryName = entry.getKey();
            List<PantryItemWithDetails> pantryItems = entry.getValue();

            // Add header
            groupedItems.add(new GroupedItem.HeaderItem(pantryName, pantryItems.size()));

            // Add items
            for (PantryItemWithDetails item : pantryItems) {
                groupedItems.add(new GroupedItem.PantryItemWrapper(item));
            }
        }

        return groupedItems;
    }
}