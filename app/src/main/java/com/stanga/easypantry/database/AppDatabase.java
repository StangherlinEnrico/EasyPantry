package com.stanga.easypantry.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;
import androidx.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.stanga.easypantry.database.entities.Pantry;
import com.stanga.easypantry.database.entities.Product;
import com.stanga.easypantry.database.entities.PantryItem;
import com.stanga.easypantry.database.dao.PantryDao;
import com.stanga.easypantry.database.dao.ProductDao;
import com.stanga.easypantry.database.dao.PantryItemDao;
import com.stanga.easypantry.utils.Converters;

@Database(
        entities = {Pantry.class, Product.class, PantryItem.class},
        version = 2,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract PantryDao pantryDao();
    public abstract ProductDao productDao();
    public abstract PantryItemDao pantryItemDao();

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                PantryDao pantryDao = INSTANCE.pantryDao();
                ProductDao productDao = INSTANCE.productDao();
                PantryItemDao pantryItemDao = INSTANCE.pantryItemDao();
                populateInitialData(pantryDao, productDao, pantryItemDao);
            });
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "easypantry_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void populateInitialData(PantryDao pantryDao, ProductDao productDao, PantryItemDao pantryItemDao) {
        // Create pantries
        pantryDao.insertPantry(new Pantry("Dispensa cucina"));
        pantryDao.insertPantry(new Pantry("Dispensa soggiorno"));

        // Add products
        productDao.insertProduct(new Product("Farina senza glutine", "Caputo"));
        productDao.insertProduct(new Product("Farina tipo 0", "Vigevano"));
        productDao.insertProduct(new Product("Pizzeria - Farina tipo 0", "Caputo"));
        productDao.insertProduct(new Product("Nuvola - Farina tipo 00", "Caputo"));
        productDao.insertProduct(new Product("Farina tipo 0", "Molino Cosma"));
        productDao.insertProduct(new Product("Farina tipo 00", "Molino Cosma"));
        productDao.insertProduct(new Product("Farina di riso", "Rossetto"));
        productDao.insertProduct(new Product("Farina di ceci", "Rossetto"));
        productDao.insertProduct(new Product("Farina integrale di farro", "Rachello"));
        productDao.insertProduct(new Product("Farina di grano saraceno", "Ruggeri"));
        productDao.insertProduct(new Product("Farina d'avena", "Rossetto"));
        productDao.insertProduct(new Product("Semola rimacinata", "Caputo"));
        productDao.insertProduct(new Product("Farina tipo 00", "Belbake"));

        // Add pantry items (assuming IDs start from 1)
        // Dispensa cucina (pantry_id = 1)
        pantryItemDao.insertPantryItem(new PantryItem(1, 1, 2, "kg")); // Farina senza glutine Caputo
        pantryItemDao.insertPantryItem(new PantryItem(3, 1, 1, "kg")); // Pizzeria Farina Caputo
        pantryItemDao.insertPantryItem(new PantryItem(4, 1, 3, "kg")); // Nuvola Farina Caputo
        pantryItemDao.insertPantryItem(new PantryItem(7, 1, 1, "confezione")); // Farina di riso Rossetto
        pantryItemDao.insertPantryItem(new PantryItem(8, 1, 2, "confezione")); // Farina di ceci Rossetto
        pantryItemDao.insertPantryItem(new PantryItem(12, 1, 1, "kg")); // Semola rimacinata Caputo

        // Dispensa soggiorno (pantry_id = 2)
        pantryItemDao.insertPantryItem(new PantryItem(2, 2, 2, "kg")); // Farina tipo 0 Vigevano
        pantryItemDao.insertPantryItem(new PantryItem(5, 2, 1, "kg")); // Farina tipo 0 Molino Cosma
        pantryItemDao.insertPantryItem(new PantryItem(6, 2, 1, "kg")); // Farina tipo 00 Molino Cosma
        pantryItemDao.insertPantryItem(new PantryItem(9, 2, 1, "confezione")); // Farina integrale farro Rachello
        pantryItemDao.insertPantryItem(new PantryItem(10, 2, 1, "confezione")); // Farina grano saraceno Ruggeri
        pantryItemDao.insertPantryItem(new PantryItem(11, 2, 2, "confezione")); // Farina d'avena Rossetto
        pantryItemDao.insertPantryItem(new PantryItem(13, 2, 1, "kg")); // Farina tipo 00 Belbake
    }
}