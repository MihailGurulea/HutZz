package io.ideaction.hutzz.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import io.ideaction.hutzz.activities.LogInActivity;

public class UtilsNetwork extends AsyncTask<Void, Void, Boolean> {

    private Activity mActivity;
    private NetworkListener mNetworkListener;

    public UtilsNetwork(Activity activity, NetworkListener networkListener) {
        mActivity = activity;
        mNetworkListener = networkListener;
    }

    @Override
    protected void onPreExecute() {
        if (mActivity instanceof LogInActivity) {
            ((LogInActivity) mActivity).showProgressBar();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return isNetworkAvailable();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting() && isOnline();
    }

    private boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            return p1.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (mActivity instanceof LogInActivity) {
            ((LogInActivity) mActivity).hideProgressBar();
        }
        mNetworkListener.hasNetwork(aBoolean);
    }

    public interface NetworkListener {
        void hasNetwork(boolean isNetwork);
    }
}
