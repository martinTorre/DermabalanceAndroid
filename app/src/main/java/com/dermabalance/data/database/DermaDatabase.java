package com.dermabalance.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.dermabalance.DermaApplication;
import com.dermabalance.data.Product;
import com.dermabalance.data.database.dao.ProductDao;

@Database(entities = {Product.class}, version = 1)
public abstract class DermaDatabase extends RoomDatabase {

    private static DermaDatabase INSTANCE;

    public abstract ProductDao productDao();

    public static DermaDatabase getDatabase() {
        if (INSTANCE == null) {
            synchronized (DermaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(DermaApplication.getInstance(),
                            DermaDatabase.class, "derma_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
