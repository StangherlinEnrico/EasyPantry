package com.stanga.easypantry.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.stanga.easypantry.database.dao.PantryItemDao;
import com.stanga.easypantry.database.entities.Pantry;
import com.stanga.easypantry.database.entities.PantryItem;
import com.stanga.easypantry.database.entities.Product;
import com.stanga.easypantry.database.dao.PantryDao;
import com.stanga.easypantry.database.dao.ProductDao;
import com.stanga.easypantry.utils.Converters;

@Database(
        entities = {Pantry.class, Product.class, PantryItem.class},
        version = 1,
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
                populateInitialData(pantryDao, productDao);
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

    private static void populateInitialData(PantryDao pantryDao, ProductDao productDao) {
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

        // Add pantry items with expiry dates
        PantryItemDao pantryItemDao = INSTANCE.pantryItemDao();

        // Helper per creare date future
        Calendar calendar = Calendar.getInstance();

        // Dispensa cucina (pantry_id = 1)
        PantryItem item1 = new PantryItem(1, 1, 1000); // Farina senza glutine Caputo - 1kg
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 8);
        item1.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item1);

        PantryItem item2 = new PantryItem(3, 1, 2500); // Pizzeria Caputo - 2.5kg
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 12);
        item2.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item2);

        PantryItem item3 = new PantryItem(4, 1, 500); // Nuvola Caputo - 500g
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 10);
        item3.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item3);

        PantryItem item4 = new PantryItem(7, 1, 300); // Farina di riso - 300g
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 6);
        item4.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item4);

        PantryItem item5 = new PantryItem(8, 1, 250); // Farina di ceci - 250g
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 9);
        item5.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item5);

        PantryItem item6 = new PantryItem(12, 1, 1000); // Semola rimacinata - 1kg
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 15);
        item6.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item6);

        // Dispensa soggiorno (pantry_id = 2)
        PantryItem item7 = new PantryItem(2, 2, 1000); // Farina tipo 0 Vigevano - 1kg
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 7);
        item7.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item7);

        PantryItem item8 = new PantryItem(5, 2, 5000); // Farina tipo 0 Cosma - 5kg
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 18);
        item8.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item8);

        PantryItem item9 = new PantryItem(6, 2, 1000); // Farina tipo 00 Cosma - 1kg
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 11);
        item9.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item9);

        PantryItem item10 = new PantryItem(9, 2, 400); // Farina farro - 400g
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 5);
        item10.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item10);

        PantryItem item11 = new PantryItem(10, 2, 350); // Farina grano saraceno - 350g
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 4);
        item11.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item11);

        PantryItem item12 = new PantryItem(11, 2, 200); // Farina d'avena - 200g
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 14);
        item12.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item12);

        PantryItem item13 = new PantryItem(13, 2, 1000); // Farina tipo 00 Belbake - 1kg
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 16);
        item13.expiryDate = calendar.getTime();
        pantryItemDao.insertPantryItem(item13);
    }
}