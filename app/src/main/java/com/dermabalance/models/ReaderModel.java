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
    public Product getProduct(final String barcode) {
        return productDao.getByBarcode(barcode);
    }

    @Override
    public void getProductsLike(String barcode) {
        new GetProductsLikeTask(barcode).execute();
    }

    @Override
    public void getProductByBarcode(final String barcode) {
        new GetProductByBarcodeTask(barcode).execute();
    }

    class GetProductsLikeTask extends AsyncTask<Void, Void, Void> {

        private String barcode;
        private List<Product> products;

        public GetProductsLikeTask(final String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            products = productDao.getByDescriptionLike(barcode);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            presenter.productsLikeGot(products);
        }
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

    class GetProductByBarcodeTask extends AsyncTask<Void, Void, Void> {

        private Product product;

        private String barcode;

        public GetProductByBarcodeTask(final String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            product = productDao.getByBarcode(barcode);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            presenter.productGot(product);
        }
    }
}
