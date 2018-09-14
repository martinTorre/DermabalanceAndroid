package com.dermabalance.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dermabalance.R;
import com.dermabalance.data.Product;
import com.dermabalance.interfaces.Reader;
import com.dermabalance.presenters.ReaderPresenter;
import com.dermabalance.views.ProductsChangedActivity;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements Filterable, Reader.View{

    /**Order list.*/
    private List<Product> productList;

    private Context context;

    private Reader.Presenter presenter;

    /**
     * Constructor.
     * @param productList to fill
     */
    public ProductsAdapter(final List<Product> productList, final Context context) {
        this.productList = productList;
        this.context = context;
        presenter = new ReaderPresenter(this);
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductsChangedActivity.start(context, product);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    presenter.getProducts(constraint.toString());
                    if (productList != null) {
                        // The API successfully returned results.
                        results.values = productList;
                        results.count = productList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    public void update(final List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @Override
    public void showProducts(List<Product> products) {

    }

    @Override
    public void showNoFile(boolean showText) {

    }

    @Override
    public void showChanges(List<Product> products) {

    }

    @Override
    public void productsLikeGot(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @Override
    public void productGot(Product product) {

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
