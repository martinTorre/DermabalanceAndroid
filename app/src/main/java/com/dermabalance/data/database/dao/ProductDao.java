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

    @Query("SELECT * FROM products WHERE changed = 1")
    List<Product> getChanged();
}
