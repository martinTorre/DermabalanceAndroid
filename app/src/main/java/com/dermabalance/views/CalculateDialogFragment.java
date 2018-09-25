package com.dermabalance.views;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.dermabalance.R;
import com.dermabalance.data.Product;
import com.dermabalance.utils.ConvertUtils;

public class CalculateDialogFragment extends DialogFragment {

    private static final String ARG_PARAM_PROD = "arg_param_prod";

    private TextView textViewFinal, textViewCalculated;
    private EditText editTextBase, editTextUtility;
    private SwitchCompat switchIva;

    private Product product;

    private double basePrice, utility;
    private static final double IVA = 0.16;

    public static CalculateDialogFragment newInstance(final Product product) {
        final CalculateDialogFragment calculateDialogFragment = new CalculateDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM_PROD, product);
        calculateDialogFragment.setArguments(bundle);
        return calculateDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = (Product) getArguments().getSerializable(ARG_PARAM_PROD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_calculate, container, false);

        textViewFinal = v.findViewById(R.id.textView_final);
        textViewCalculated = v.findViewById(R.id.textView_calculated);
        editTextBase = v.findViewById(R.id.editText_base);
        editTextUtility = v.findViewById(R.id.editText_utility);
        switchIva = v.findViewById(R.id.iva);

        setData();
        setListeners();

        return v;
    }

    private void setData() {
        if (product != null) {
            textViewFinal.setText(product.getPrice() + "");
        }
    }

    private void setListeners() {
        editTextBase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                basePrice = ConvertUtils.getDouble(s.toString());
                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextUtility.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                utility = ConvertUtils.getDouble(s.toString());
                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        switchIva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calculate();
            }
        });
    }

    private void calculate() {
        double total = basePrice
                + (switchIva.isChecked() ? (basePrice * IVA) : 0);

        total = total + (total * (utility / 100));

        textViewCalculated.setText(total + "");
    }
}
