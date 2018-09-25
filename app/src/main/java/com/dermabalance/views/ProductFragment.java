package com.dermabalance.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dermabalance.R;
import com.dermabalance.data.Product;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    private static final String ARG_PARAM_PRODUCT = "param_product";

    private Product product;

    private TextView textViewDescription, textViewBarcode, textViewLastPrice, textViewNewPrice, textViewDifference;

    private Button btnCalculate;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param product Parameter 1.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(Product product) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PARAM_PRODUCT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_product, container, false);

        initUI(view);
        setData();

        return view;
    }

    private void initUI(final View view) {
        textViewDescription = view.findViewById(R.id.description);
        textViewBarcode = view.findViewById(R.id.barcode);
        textViewLastPrice = view.findViewById(R.id.last_price);
        textViewNewPrice = view.findViewById(R.id.new_price);
        textViewDifference = view.findViewById(R.id.difference);
        btnCalculate = view.findViewById(R.id.calculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CalculateDialogFragment dialog = CalculateDialogFragment.newInstance(product);
                dialog.show(getFragmentManager(), "");
            }
        });
    }

    private void setData() {
        if (product != null) {
            textViewDescription.setText(product.getDescription() + "");
            textViewBarcode.setText(product.getBarcode() + "");
            textViewLastPrice.setText(product.getLastPrice() + "");
            textViewNewPrice.setText(product.getPrice() + "");
            textViewDifference.setText(product.getDifference() + "");
        }
    }
}
