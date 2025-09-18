package com.stanga.easypantry.ui.product;

import com.stanga.easypantry.database.entities.Product;

public interface OnProductClickListener {
    void onProductClick(Product product);
    void onProductLongClick(Product product);
}