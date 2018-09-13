package com.dermabalance.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.dermabalance.R;

public class ProgressDialogUtils {

    /**
     * Progress.
     */
    private static ProgressDialog progressDialog;

    /**
     * It will show circle progress.
     * @param context to show
     */
    public static void show(final Context context) {
        try {
            progressDialog = ProgressDialog.show(context, null, null, false, false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_bar);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * It will dismiss progress.
     */
    public static void dismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
