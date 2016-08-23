package com.home.vlas.shops.utils;

import android.app.Application;

public class ShopApplication extends Application {
    private static ShopApplication mInstance;

    public static synchronized ShopApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
