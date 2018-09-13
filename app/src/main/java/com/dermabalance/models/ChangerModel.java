package com.dermabalance.models;

import android.os.AsyncTask;

import com.dermabalance.data.Product;
import com.dermabalance.data.database.DermaDatabase;
import com.dermabalance.data.database.dao.ProductDao;
import com.dermabalance.interfaces.Changer;

import java.util.List;

public class ChangerModel implements Changer.Model {

    private Changer.Presenter presenter;

    private DermaDatabase database;

    private ProductDao productDao;

    public ChangerModel(Changer.Presenter presenter) {
        this.presenter = presenter;
        database = DermaDatabase.getDatabase();
        productDao = database.productDao();
    }

    @Override
    public void getProductsChanged() {
        new GetProductsChangedTask().execute();
    }

    class GetProductsChangedTask extends AsyncTask<Void, Void, Void> {

        private List<Product> products;

        @Override
        protected Void doInBackground(Void... voids) {
            products = productDao.getChanged();
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            presenter.productsGot(products);
        }
    }
}
