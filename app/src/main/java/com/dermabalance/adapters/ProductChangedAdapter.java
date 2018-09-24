package com.dermabalance.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dermabalance.data.Product;
import com.dermabalance.views.ProductFragment;

import java.util.List;

public class ProductChangedAdapter extends FragmentStatePagerAdapter {

    private List<Product> products;

    /**
     * Constructor.
     * @param fm fragment
     * @param products pages list
     */
    public ProductChangedAdapter(final FragmentManager fm, final List<Product> products) {
        super(fm);
        this.products = products;
    }

    @Override
    public Fragment getItem(final int position) {
        return ProductFragment.newInstance(products.get(position));
    }

    @Override
    public int getCount() {
        return products != null ? products.size() : 0;
    }

    public void update(final List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
}
