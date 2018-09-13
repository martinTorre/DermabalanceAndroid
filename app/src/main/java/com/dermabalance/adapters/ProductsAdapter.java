package com.dermabalance.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dermabalance.R;
import com.dermabalance.data.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    /**Order list.*/
    private List<Product> productList;

    private Context context;

    /**
     * Constructor.
     * @param productList to fill
     */
    public ProductsAdapter(final List<Product> productList, final Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product product = productList.get(position);
        if (product != null) {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            holder.textViewBarcode.setText(product.getBarcode() + "");
            holder.textViewDescription.setText(product.getDescription() + "");
        }
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    /**
     * To hold item view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**Texviews.*/
        public TextView textViewBarcode, textViewDescription;

        /**
         * Contructor.
         * @param itemView item
         */
        public ViewHolder(final View itemView) {
            super(itemView);
            textViewBarcode = itemView.findViewById(R.id.barcode);
            textViewDescription = itemView.findViewById(R.id.description);
        }
    }
}
