package com.dermabalance.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dermabalance.data.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Query("SELECT * from products ORDER BY barcode ASC")
    List<Product> getAll();

    @Update
    void update(Product product);

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    Product getByBarcode(String barcode);

    @Query("SELECT * FROM products WHERE description like:description")
    List<Product> getByDescriptionLike(String description);

    @Query("SELECT * FROM products WHERE changed = 1 and difference < 0 order by difference asc")
    List<Product> getChangedNegative();

    @Query("SELECT * FROM products WHERE changed = 1 and difference >= 0 order by difference desc")
    List<Product> getChangedPositive();

    @Query("UPDATE products set changed = 0 where changed = 1")
    void clearAll();
}
