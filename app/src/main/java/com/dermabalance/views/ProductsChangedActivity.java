package com.dermabalance.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.dermabalance.R;
import com.dermabalance.adapters.ProductChangedAdapter;
import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Changer;
import com.dermabalance.presenters.ChangerPresenter;
import com.dermabalance.utils.ProgressDialogUtils;

import java.util.List;

public class ProductsChangedActivity extends AppCompatActivity implements Changer.View {

    private List<Product> products;

    private ViewPager mPager;

    private ProductChangedAdapter adapter;

    private FloatingActionButton fab;

    private Changer.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_changed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
        bindListeners();
        presenter = new ChangerPresenter(this);

        ProgressDialogUtils.show(this);
        presenter.getProductsChanged();
    }

    private void initUI() {
        fab = findViewById(R.id.fab);
        mPager = findViewById(R.id.pager);
    }

    public static void start(final Context context) {
        final Intent intent = new Intent(context, ProductsChangedActivity.class);
        context.startActivity(intent);
    }

    /**Listeners for ui components.*/
    private void bindListeners() {
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                handlePosition(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogUtils.show(ProductsChangedActivity.this);
                presenter.updateProduct(products.get(mPager.getCurrentItem()));
            }
        });

    }

    private void handlePosition(final int position) {
        getSupportActionBar().setTitle((position + 1) + " de " + products.size());
    }

    @Override
    public void productsGot(List<Product> products) {
        if (products != null && products.size() > 0) {
            this.products = products;
            adapter = new ProductChangedAdapter(getSupportFragmentManager(), products);
            mPager.setAdapter(adapter);
            handlePosition(0);
        }

        ProgressDialogUtils.dismiss();
        Toast.makeText(this, getString(R.string.file_readed_no_changes), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void productUpdated(final List<Product> products) {
        ProgressDialogUtils.dismiss();
        this.products = products;
        if (products != null && products.size() > 0) {
            ProductsChangedActivity.start(this);
            finish();
        } else {
            finish();
        }
    }
}
