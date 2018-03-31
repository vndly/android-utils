package com.mauriciotogneri.androidutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connectivity
{
    private final ConnectivityManager connectivityManager;

    public enum Status
    {
        WIFI,
        MOBILE,
        NOT_CONNECTED
    }

    public Connectivity(Context context)
    {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @SuppressLint("MissingPermission")
    private Status status()
    {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if ((activeNetwork != null) && activeNetwork.isConnectedOrConnecting())
        {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            {
                return Status.WIFI;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                return Status.MOBILE;
            }
        }

        return Status.NOT_CONNECTED;
    }

    public boolean isConnected()
    {
        Status status = status();

        return (status == Status.MOBILE) || (status == Status.WIFI);
    }

    public boolean isConnectivityWifi()
    {
        return (status() == Status.WIFI);
    }

    public boolean isConnectivityMobile()
    {
        return (status() == Status.MOBILE);
    }

    public boolean hasInternetConnection()
    {
        return (status() != Status.NOT_CONNECTED);
    }
}