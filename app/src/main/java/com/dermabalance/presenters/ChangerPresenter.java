package com.dermabalance.presenters;

import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Changer;
import com.dermabalance.models.ChangerModel;

import java.util.List;

public class ChangerPresenter implements Changer.Presenter {

    private Changer.View view;

    private Changer.Model model;

    public ChangerPresenter(Changer.View view) {
        this.view = view;
        model = new ChangerModel(this);
    }

    @Override
    public void getProductsChanged() {
        model.getProductsChanged(false);
    }

    @Override
    public void productsGot(final List<Product> products, final boolean wasUpdated) {
        if (wasUpdated) {
            view.productUpdated(products);
        } else {
            view.productsGot(products);
        }
    }

    @Override
    public void updateProduct(final Product product) {
        product.setChanged(0);
        model.updateProduct(product);
    }

    @Override
    public void productUpdated() {
        model.getProductsChanged(true);
    }
}
