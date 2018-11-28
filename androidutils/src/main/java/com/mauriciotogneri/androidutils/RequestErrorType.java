package com.mauriciotogneri.androidutils;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public enum RequestErrorType
{
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    CONFLICT(409),
    PRECONDITION_FAILED(412),
    TOO_MANY_REQUESTS(429),
    INTERNAL_SERVER_ERROR(500),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504),
    NO_INTERNET(0),
    NETWORK_TIMEOUT(0),
    UNKNOWN(0);

    private final int code;

    RequestErrorType(int code)
    {
        this.code = code;
    }

    public int code()
    {
        return code;
    }

    public RequestErrorType fromCode(int responseCode)
    {
        for (RequestErrorType type : RequestErrorType.values())
        {
            if (type.code() == responseCode)
            {
                return type;
            }
        }

        return UNKNOWN;
    }

    public static RequestErrorType fromException(Throwable throwable, Context context)
    {
        Connectivity connectivity = new Connectivity(context);

        if (noInternetException(throwable) && !connectivity.isConnected())
        {
            return NO_INTERNET;
        }
        else if (timeoutException(throwable) && connectivity.isConnected())
        {
            return NETWORK_TIMEOUT;
        }
        else
        {
            return UNKNOWN;
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

    private static boolean isException(Throwable throwable, Class<? extends Throwable> exception)
    {
        return (exception.isInstance(throwable)) || throwable.toString().contains(exception.getSimpleName());
    }
}