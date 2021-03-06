package com.dermabalance.interfaces;

import com.dermabalance.data.Product;

import java.util.List;

public interface Changer {

    /**View interface.*/
    interface View {
        void productsGot(List<Product> productsHigher, List<Product> productsLower);

        void productUpdated(List<Product> productsHigher, List<Product> productsLower);

        void allCleared();
    }

    /**Presenter interface.*/
    interface Presenter {
        void getProductsChanged();

        void productsGot(List<Product> productsHigher, List<Product> productsLower, boolean wasUpdated);

        void updateProduct(Product product);

        void productUpdated();

        void clearAll();

        void allCleared();
    }

    /**Model interface.*/
    interface Model {
        void getProductsChanged(boolean wasUpdated);

        void updateProduct(Product product);

        void clearAll();
    }
}
