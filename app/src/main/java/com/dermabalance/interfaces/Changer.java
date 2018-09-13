package com.dermabalance.interfaces;

import com.dermabalance.data.Product;

import java.util.List;

public interface Changer {

    /**View interface.*/
    interface View {
        void productsGot(List<Product> products);
    }

    /**Presenter interface.*/
    interface Presenter {
        void getProductsChanged();

        void productsGot(List<Product> products);
    }

    /**Model interface.*/
    interface Model {
        void getProductsChanged();
    }
}
