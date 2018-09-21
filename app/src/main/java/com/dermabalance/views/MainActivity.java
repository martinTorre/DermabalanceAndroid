package com.dermabalance.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dermabalance.R;
import com.dermabalance.adapters.ProductsAdapter;
import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Reader;
import com.dermabalance.presenters.ReaderPresenter;
import com.dermabalance.utils.ProgressDialogUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Reader.View {

    private static final int PICKFILE_RESULT_CODE = 200;

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private ProductsAdapter adapter;

    private Reader.Presenter presenter;

    private TextView textViewInfo;

    private EditText editTextBarcode;

    private DrawerLayout drawerLayout;

    private List<Product> products;

    private boolean isThereProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createToolbar();
        initUI();
        setListeners();

        ProgressDialogUtils.show(this);
        presenter = new ReaderPresenter(this);
        presenter.getProducts();
    }

    /**
     * Init UI components.
     */
    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        textViewInfo = findViewById(R.id.textview_info);
        drawerLayout = findViewById(R.id.drawer_layout);
        editTextBarcode = findViewById(R.id.barcode);

        //Init list
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setListeners() {
        editTextBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (adapter != null) {
                    if (s.toString().isEmpty() && products != null) {
                        adapter.update(products);
                    } else {
                        adapter.getFilter().filter(s.toString());
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void showProducts(final List<Product> products) {
        if (products != null && products.size() > 0) {
            this.products = products;
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ProductsAdapter(products, MainActivity.this);
            recyclerView.setAdapter(adapter);
            textViewInfo.setVisibility(View.GONE);
            final Snackbar snackbar = Snackbar
                    .make(recyclerView, products.size() + " " + getString(R.string.products), Snackbar.LENGTH_LONG);
            snackbar.show();

            isThereProducts = true;

        } else {
            textViewInfo.setVisibility(View.VISIBLE);
        }

        ProgressDialogUtils.dismiss();
    }

    @Override
    public void showNoFile(final boolean showText) {
        if (showText) {
            textViewInfo.setText(textViewInfo.getText() + " " + getString(R.string.no_file));
        }
        ProgressDialogUtils.dismiss();
    }

    @Override
    public void showChanges(final List<Product> products) {
        ProgressDialogUtils.dismiss();
        if (products != null && products.size() > 0) {
            goToNewPrices(null);
        } else {
            final Snackbar snackbar = Snackbar
                    .make(recyclerView, products.size() + " " + getString(R.string.file_readed_no_changes), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void productsLikeGot(final List<Product> products) {

    }

    @Override
    public void productGot(final Product product) {
        ProgressDialogUtils.dismiss();
        if (product != null) {
            ProductsChangedActivity.start(this, product);
        } else {
            final Snackbar snackbar = Snackbar
                    .make(recyclerView, getString(R.string.no_product_found), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        drawerLayout.openDrawer(Gravity.LEFT);
        return true;
    }

    private void createToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void goToNewPrices(final View view) {
        ProductsChangedActivity.start(this, null);
    }

    public void scan(final View view) {
        startActivityForResult(new Intent(this, BarcodeScannerActivity.class), BarcodeScannerActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // Check which request we're responding to
        if (requestCode == BarcodeScannerActivity.REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ProgressDialogUtils.show(this);
                try {
                    presenter.getProduct(data.getStringExtra(BarcodeScannerActivity.EXTRA_KEY));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == BarcodeScannerActivity.RESULT_ACCEPTED_PERMISSION) {
                startActivityForResult(new Intent(this, BarcodeScannerActivity.class), BarcodeScannerActivity.REQUEST_CODE);
            }
        } else {
            if (resultCode == RESULT_OK) {
                final Uri uri = data.getData();
                ProgressDialogUtils.show(this);
                presenter.readExcelData(isThereProducts ? ReaderPresenter.UPDATE : ReaderPresenter.INSERT, uri);
            }
        }
    }

    public void addFile(final View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }
}
