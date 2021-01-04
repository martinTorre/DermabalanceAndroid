package com.dermabalance.views;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dermabalance.R;

public class DeleteDataDialogFragment extends DialogFragment {

    private Button buttonYes;
    private Button buttonNo;

    public static DeleteDataDialogFragment newInstance() {
        final DeleteDataDialogFragment deleteDataDialogFragment = new DeleteDataDialogFragment();
        return deleteDataDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_delete, container, false);
        buttonYes = v.findViewById(R.id.button_yes);
        buttonNo = v.findViewById(R.id.button_no);
        setListeners();

        return v;
    }

    private void setListeners() {
        buttonYes.setOnClickListener(view -> {
            ((MainActivity) getActivity()).deleteDataConfirmed();
            dismiss();
        });

        buttonNo.setOnClickListener(view -> dismiss());
    }
}
