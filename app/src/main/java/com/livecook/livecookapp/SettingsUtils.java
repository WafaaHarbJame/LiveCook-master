package com.livecook.livecookapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SettingsUtils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        }
        return false;
    }

}
