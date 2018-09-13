package com.dermabalance.interfaces;

import com.dermabalance.data.Product;

import java.util.List;

public interface Reader {

    /**View interface.*/
    interface View {
        void showProducts(List<Product> products);

        void showNoFile(boolean showText);

        void showChanges(List<Product> products);
    }

    /**Presenter interface.*/
    interface Presenter {
        void getProducts();

        void productsGot(List<Product> products);

        void readExcelData(int type);
    }

    /**Model interface.*/
    interface Model {
        void getProducts();

        void insertProducts(List<Product> products);

        void insertProduct(Product product);

        void update(Product product);

        Product getByBarcode(String barcode);
    }
}
