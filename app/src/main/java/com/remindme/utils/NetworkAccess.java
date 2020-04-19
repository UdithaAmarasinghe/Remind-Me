package com.remindme.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkAccess {
    private static Context mContext;

    public NetworkAccess(Context context) {
        mContext = context;
    }

    public static boolean isNetworkAvailable(final Context context/*, LzColoredSnackBar coloredSnackBar*/) {

        mContext = context;

        boolean state = isInternetAvailable();

        if (!state) {

            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();

        }

        return state;
    }

    private static boolean isInternetAvailable() {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
