package com.stanga.easypantry.database.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.stanga.easypantry.database.AppDatabase;
import com.stanga.easypantry.database.dao.ProductDao;
import com.stanga.easypantry.database.entities.Product;

import java.util.List;

public class ProductRepository {
    private final ProductDao productDao;
    private final LiveData<List<Product>> allProducts;

    public ProductRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<Product> getProductById(int id) {
        return productDao.getProductById(id);
    }

    public LiveData<List<Product>> searchProducts(String searchQuery) {
        return productDao.searchProducts("%" + searchQuery + "%");
    }

    public void insert(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.insertProduct(product);
        });
    }

    public void update(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.updateProduct(product);
        });
    }

    public void delete(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            productDao.deleteProduct(product);
        });
    }
}