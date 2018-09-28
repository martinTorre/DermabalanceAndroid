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
    public void getProductsChanged(final boolean wasUpdated) {
        new GetProductsChangedTask(wasUpdated).execute();
    }

    @Override
    public void updateProduct(Product product) {
        new UpdateProductTask(product).execute();
    }

    @Override
    public void clearAll() {
        new UpdateProductsTask().execute();
    }

    class GetProductsChangedTask extends AsyncTask<Void, Void, Void> {

        private List<Product> productsHigher, productsLower;
        private boolean wasUpdated;

        public GetProductsChangedTask(final boolean wasUpdated) {
            this.wasUpdated = wasUpdated;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            productsHigher = productDao.getChangedPositive();
            productsLower = productDao.getChangedNegative();
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            presenter.productsGot(productsHigher, productsLower, wasUpdated);
        }
    }

    class UpdateProductTask extends AsyncTask<Void, Void, Void> {

        private Product product;

        public UpdateProductTask(final Product product) {
            this.product = product;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            productDao.update(product);
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            presenter.productUpdated();
        }
    }

    class UpdateProductsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            productDao.clearAll();
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            presenter.allCleared();
        }
    }
}
