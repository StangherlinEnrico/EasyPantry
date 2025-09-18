package com.stanga.easypantry.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stanga.easypantry.database.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :id")
    LiveData<Product> getProductById(int id);

    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("DELETE FROM products WHERE id = :id")
    void deleteProductById(int id);

    @Query("SELECT * FROM products WHERE name LIKE :searchQuery OR brand LIKE :searchQuery ORDER BY name ASC")
    LiveData<List<Product>> searchProducts(String searchQuery);
}