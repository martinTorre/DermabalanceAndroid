package com.dermabalance.models;

import android.os.AsyncTask;

import com.dermabalance.data.Product;
import com.dermabalance.data.database.DermaDatabase;
import com.dermabalance.data.database.dao.ProductDao;
import com.dermabalance.interfaces.Reader;

import java.util.List;

public class ReaderModel implements Reader.Model {

    private Reader.Presenter presenter;

    private DermaDatabase database;

    private ProductDao productDao;

    public ReaderModel(Reader.Presenter presenter) {
        this.presenter = presenter;
        database = DermaDatabase.getDatabase();
        productDao = database.productDao();
    }

    @Override
    public void getProducts() {
        new GetProductsTask().execute();
    }

    @Override
    public void insertProducts(final List<Product> products) {
        new InsertProductsTask(products).execute();
    }

    @Override
    public void insertProduct(Product product) {
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public Product getByBarcode(final String barcode) {
        return productDao.getByBarcode(barcode);
    }

    class GetProductsTask extends AsyncTask<Void, Void, Void> {

        private List<Product> products;

        @Override
        protected Void doInBackground(Void... voids) {
            products = productDao.getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            presenter.productsGot(products);
        }
    }

    class InsertProductsTask extends AsyncTask<Void, Void, Void> {

        private List<Product> products;

        public InsertProductsTask(final List<Product> products) {
            this.products = products;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (Product product : products) {
                productDao.insert(product);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            presenter.productsGot(products);
        }
    }
}
