package com.dermabalance.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.dermabalance.R;
import com.dermabalance.adapters.ProductsAdapter;
import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Reader;
import com.dermabalance.presenters.ReaderPresenter;
import com.dermabalance.utils.ProgressDialogUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Reader.View {
    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private ProductsAdapter adapter;

    private Reader.Presenter presenter;

    private TextView textViewInfo;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createToolbar();
        initUI();

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

        //Init list
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void showProducts(final List<Product> products) {
        if (products != null && products.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ProductsAdapter(products, MainActivity.this);
            recyclerView.setAdapter(adapter);
            textViewInfo.setVisibility(View.GONE);
            ProgressDialogUtils.dismiss();
            final Snackbar snackbar = Snackbar
                    .make(recyclerView, products.size() + " " + getString(R.string.products), Snackbar.LENGTH_LONG);
            snackbar.show();

            ProgressDialogUtils.show(this);
            presenter.readExcelData(ReaderPresenter.UPDATE);

        } else {
            textViewInfo.setVisibility(View.VISIBLE);
            presenter.readExcelData(ReaderPresenter.INSERT);
        }
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
        ProductsChangedActivity.start(this);
    }
}