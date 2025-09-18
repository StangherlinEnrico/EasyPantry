package com.stanga.easypantry.ui.product;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import java.util.List;

import com.stanga.easypantry.database.entities.Product;
import com.stanga.easypantry.database.repositories.ProductRepository;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repository;
    private LiveData<List<Product>> allProducts;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();

    public ProductViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> getSearchResults() {
        return Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                return allProducts;
            } else {
                return repository.searchProducts(query);
            }
        });
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void insert(Product product) {
        repository.insert(product);
    }

    public void update(Product product) {
        repository.update(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }
}