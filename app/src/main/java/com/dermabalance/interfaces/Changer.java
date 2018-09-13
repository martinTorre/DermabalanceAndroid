package com.dermabalance.interfaces;

import com.dermabalance.data.Product;

import java.util.List;

public interface Changer {

    /**View interface.*/
    interface View {
        void productsGot(List<Product> products);

        void productUpdated(List<Product> products);
    }

    /**Presenter interface.*/
    interface Presenter {
        void getProductsChanged();

        void productsGot(List<Product> products, boolean wasUpdated);

        void updateProduct(Product product);

        void productUpdated();
    }

    /**Model interface.*/
    interface Model {
        void getProductsChanged(boolean wasUpdated);

        void updateProduct(Product product);
    }
}
