package com.mauriciotogneri.androidutils;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import androidx.annotation.NonNull;

public class RequestErrorType
{
    public enum Source
    {
        HTTP,
        NO_INTERNET,
        NETWORK_TIMEOUT,
        UNKNOWN
    }

    private final int code;
    private final Source source;

    RequestErrorType(int code, Source source)
    {
        this.code = code;
        this.source = source;
    }

    RequestErrorType(Source source)
    {
        this.code = 0;
        this.source = source;
    }

    public int code()
    {
        return code;
    }

    public Source source()
    {
        return source;
    }

    @NonNull
    public static RequestErrorType fromCode(int responseCode)
    {
        return new RequestErrorType(responseCode, Source.HTTP);
    }

    @NonNull
    public static RequestErrorType fromException(Throwable throwable, Context context)
    {
        Connectivity connectivity = new Connectivity(context);

        if (noInternetException(throwable) && !connectivity.isConnected())
        {
            return new RequestErrorType(Source.NO_INTERNET);
        }
        else if (timeoutException(throwable) && connectivity.isConnected())
        {
            return new RequestErrorType(Source.NETWORK_TIMEOUT);
        }
        else
        {
            return new RequestErrorType(Source.UNKNOWN);
        }
    }

    private static boolean noInternetException(Throwable throwable)
    {
        return (isException(throwable, UnknownHostException.class) ||
                isException(throwable, ConnectException.class) ||
                isException(throwable, SocketException.class));
    }

    private static boolean timeoutException(Throwable throwable)
    {
        return isException(throwable, SocketTimeoutException.class);
    }

    private static boolean isException(@NonNull Throwable throwable, @NonNull Class<? extends Throwable> exception)
    {
        return (exception.isInstance(throwable)) || throwable.toString().contains(exception.getSimpleName());
    }
}