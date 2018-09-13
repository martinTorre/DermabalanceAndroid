package com.dermabalance;

import android.app.Application;

public class DermaApplication extends Application {

    private static DermaApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    /**
     * Return application object.
     * @return singleton
     */
    public static DermaApplication getInstance() {
        return INSTANCE;
    }
}
