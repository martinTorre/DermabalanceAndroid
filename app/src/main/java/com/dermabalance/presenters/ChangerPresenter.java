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
        model.getProductsChanged();
    }

    @Override
    public void productsGot(List<Product> products) {
        view.productsGot(products);
    }
}
