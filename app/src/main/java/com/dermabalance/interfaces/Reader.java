package com.dermabalance.interfaces;

import android.net.Uri;

import com.dermabalance.data.Product;

import java.util.List;

public interface Reader {

    /**View interface.*/
    interface View {
        void showProducts(List<Product> products);

        void showNoFile(boolean showText);

        void showChanges(List<Product> products);

        void productsLikeGot(List<Product> products);

        void productGot(Product product);

        void dataDeleted();
    }

    /**Presenter interface.*/
    interface Presenter {
        void getProducts(String description);

        void productsLikeGot(List<Product> products);

        void getProducts();

        void productsGot(List<Product> products);

        void readExcelData(int type, Uri filePathUri);

        void getProduct(String barcode);

        void productGot(Product product);

        void deleteData();

        void dataDeleted();
    }

    /**Model interface.*/
    interface Model {
        void getProducts();

        void insertProducts(List<Product> products);

        void insertProduct(Product product);

        void update(Product product);

        Product getProduct(String barcode);

        void getProductsLike(String barcode);

        void getProductByBarcode(String barcode);

        void deleteData();
    }
}
