package com.dermabalance.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dermabalance.R;
import com.dermabalance.adapters.ProductChangedAdapter;
import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Changer;
import com.dermabalance.presenters.ChangerPresenter;
import com.dermabalance.utils.ProgressDialogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductsChangedActivity extends AppCompatActivity implements Changer.View {

    private static final String ARG_PARAM_PROD = "arg_prod", ARG_PARAM_TAB = "arg_tab", ARG_PARAM_POS = "arg_pos";

    private List<Product> productsHigher, productsLower;

    private ViewPager mPagerHigher, mPagerLower;

    private ProductChangedAdapter adapterHigher, adapterLower;

    private FloatingActionButton fab;

    private Changer.Presenter presenter;

    private Product product;

    private TabLayout tabs;

    private boolean isLowerSelected, positionWasHandled;

    private TextView textViewPagingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_changed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
        bindListeners();
        presenter = new ChangerPresenter(this);

        product = (Product) getIntent().getSerializableExtra(ARG_PARAM_PROD);
        if (product != null) {
            productsHigher = new ArrayList<>();
            productsHigher.add(product);
            tabs.setVisibility(View.GONE);
            textViewPagingIndicator.setVisibility(View.GONE);
            fab.hide();
            productsGot(productsHigher, null);
        } else {
            ProgressDialogUtils.show(this);
            presenter.getProductsChanged();
        }
    }

    public static void start(final Context context, final Product product) {
        final Intent intent = new Intent(context, ProductsChangedActivity.class);
        intent.putExtra(ARG_PARAM_PROD, (Serializable) product);
        context.startActivity(intent);
    }

    public static void start(final Context context, final int tab, final int pos) {
        final Intent intent = new Intent(context, ProductsChangedActivity.class);
        intent.putExtra(ARG_PARAM_PROD, (Serializable) null);
        intent.putExtra(ARG_PARAM_TAB, tab);
        intent.putExtra(ARG_PARAM_POS, pos);
        context.startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initUI() {
        fab = findViewById(R.id.fab);
        mPagerHigher = findViewById(R.id.pager_higher);
        mPagerLower = findViewById(R.id.pager_lower);
        tabs = findViewById(R.id.tabs);
        textViewPagingIndicator = findViewById(R.id.paging_indicator);

        addTabs();
    }

    private void addTabs() {
        tabs.addTab(tabs.newTab().setText(R.string.higher));
        tabs.addTab(tabs.newTab().setText(R.string.lower));
    }

    /**Listeners for ui components.*/
    private void bindListeners() {
        mPagerHigher.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                handlePosition();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        mPagerLower.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                handlePosition();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogUtils.show(ProductsChangedActivity.this);

                if (isLowerSelected) {
                    presenter.updateProduct(productsLower.get(mPagerLower.getCurrentItem()));
                } else {
                    presenter.updateProduct(productsHigher.get(mPagerHigher.getCurrentItem()));
                }
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null && tab.getText().equals(getString(R.string.lower))) {
                    isLowerSelected = true;
                    mPagerLower.setVisibility(View.VISIBLE);
                    mPagerHigher.setVisibility(View.GONE);
                } else {
                    isLowerSelected = false;
                    mPagerLower.setVisibility(View.GONE);
                    mPagerHigher.setVisibility(View.VISIBLE);
                }

                handlePosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void handlePosition() {
        if (isLowerSelected) {
            final int position = mPagerLower.getCurrentItem() + 1;
            textViewPagingIndicator.setText((position) + " de " + productsLower.size());
        } else {
            final int position = mPagerHigher.getCurrentItem() + 1;
            textViewPagingIndicator.setText((position) + " de " + productsHigher.size());
        }
    }

    @Override
    public void productsGot(List<Product> productsHigher, List<Product> productsLower) {
        ProgressDialogUtils.dismiss();
        this.productsHigher = productsHigher;
        this.productsLower = productsLower;

        if (productsHigher != null && productsHigher.size() > 0) {
            adapterHigher = new ProductChangedAdapter(getSupportFragmentManager(), productsHigher);
            mPagerHigher.setAdapter(adapterHigher);
        }

        if (productsLower != null && productsLower.size() > 0) {
            adapterLower = new ProductChangedAdapter(getSupportFragmentManager(), productsLower);
            mPagerLower.setAdapter(adapterLower);
        }

        if ((productsHigher == null || productsHigher.size() < 1)
                && (productsLower == null || productsLower.size() < 1)) {
            Toast.makeText(this, getString(R.string.file_readed_no_changes), Toast.LENGTH_LONG).show();
            finish();
        }

        if (!positionWasHandled) {
            final Intent intent =  getIntent();
            if (intent != null) {
                final int tab = intent.getIntExtra(ARG_PARAM_TAB, -1);
                final int pos = intent.getIntExtra(ARG_PARAM_POS, -1);

                if (tab > -1) {
                    tabs.getTabAt(tab).select();
                }

                if (pos > -1) {
                    if (isLowerSelected && productsLower.size() > pos) {
                        mPagerLower.setCurrentItem(pos);
                    } else if (productsHigher.size() > pos) {
                        mPagerHigher.setCurrentItem(pos);
                    }
                }
            }
            positionWasHandled = true;
        }

        handlePosition();
    }

    @Override
    public void productUpdated(final List<Product> productsHigher, final List<Product> productsLower) {
        ProgressDialogUtils.dismiss();

        if ((productsHigher == null || productsHigher.size() == 0)
                && (productsLower == null || productsLower.size() == 0)) {
            Toast.makeText(this, getString(R.string.finish), Toast.LENGTH_LONG).show();
            finish();
        } else {
            ProductsChangedActivity.start(this, isLowerSelected ? 1 : 0,
                    isLowerSelected ? mPagerLower.getCurrentItem() : mPagerHigher.getCurrentItem());
            finish();
        }

    }
}
