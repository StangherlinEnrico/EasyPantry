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
import com.stanga.easypantry.database.dao.PantryDao;
import com.stanga.easypantry.database.dao.ProductDao;
import com.stanga.easypantry.utils.Converters;

@Database(
        entities = {Pantry.class, Product.class},
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
                            .fallbackToDestructiveMigration() // For version update
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void populateInitialData(PantryDao pantryDao, ProductDao productDao) {
        // Insert default pantries
        pantryDao.insertPantry(new Pantry("Dispensa cucina", "pantry"));
        pantryDao.insertPantry(new Pantry("Dispensa soggiorno", "pantry"));
        pantryDao.insertPantry(new Pantry("Frigo cucina", "fridge"));
        pantryDao.insertPantry(new Pantry("Congelatore cucina", "freezer"));
        pantryDao.insertPantry(new Pantry("Frigo poolhouse", "fridge"));
        pantryDao.insertPantry(new Pantry("Congelatore poolhouse", "freezer"));
    }
}