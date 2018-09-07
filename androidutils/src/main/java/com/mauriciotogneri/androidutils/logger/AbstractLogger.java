package com.mauriciotogneri.androidutils.logger;

import android.util.Log;

public abstract class AbstractLogger
{
    private final boolean enabledNativeLogs;

    public enum LogLevel
    {
        VERBOSE,
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    protected AbstractLogger(boolean enabledNativeLogs)
    {
        this.enabledNativeLogs = enabledNativeLogs;
    }

    private String defaultTag()
    {
        try
        {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            String className = stackTrace[3].getClassName();

            return className.substring(className.lastIndexOf('.') + 1);
        }
        catch (Exception e)
        {
            return AbstractLogger.class.getName();
        }
    }

    protected abstract void logOk(LogLevel level, String tag, String message, Throwable exception);

    protected abstract void logOk(LogLevel level, String tag, String message);

    protected abstract void logFail(LogLevel level, Throwable e);

    // ============================ GENERIC ============================ \\

    public void log(LogLevel level, Object tag, Object message, Throwable exception)
    {
        try
        {
            if (enabledNativeLogs)
            {
                logNative(level, tag.toString(), message.toString(), exception);
            }

            logOk(level, tag.toString(), message.toString(), exception);
        }
        catch (Exception e)
        {
            logFail(level, e);
        }
    }

    public void log(LogLevel level, Object tag, Object message)
    {
        try
        {
            if (enabledNativeLogs)
            {
                logNative(level, tag.toString(), message.toString());
            }

            logOk(level, tag.toString(), message.toString());
        }
        catch (Exception e)
        {
            logFail(level, e);
        }
    }

    public void log(LogLevel level, Object tag, Throwable exception)
    {
        try
        {
            if (enabledNativeLogs)
            {
                logNative(level, tag.toString(), exception.getMessage(), exception);
            }

            logOk(level, tag.toString(), exception.getMessage(), exception);
        }
        catch (Exception e)
        {
            logFail(level, e);
        }
    }

    public void log(LogLevel level, Object message)
    {
        try
        {
            String tag = defaultTag();

            if (enabledNativeLogs)
            {
                logNative(level, tag, message.toString());
            }

            logOk(level, tag, message.toString());
        }
        catch (Exception e)
        {
            logFail(level, e);
        }
    }

    public void log(LogLevel level, Throwable exception)
    {
        try
        {
            String tag = defaultTag();

            if (enabledNativeLogs)
            {
                logNative(level, tag, exception.getMessage(), exception);
            }

            logOk(level, tag, exception.getMessage(), exception);
        }
        catch (Exception e)
        {
            logFail(level, e);
        }
    }

    public void logNative(LogLevel level, String tag, String message, Throwable throwable)
    {
        switch (level)
        {
            case VERBOSE:
                Log.v(tag, message, throwable);
                break;

            case DEBUG:
                Log.d(tag, message, throwable);
                break;

            case INFO:
                Log.i(tag, message, throwable);
                break;

            case WARNING:
                Log.w(tag, message, throwable);
                break;

            case ERROR:
                Log.e(tag, message, throwable);
                break;
        }
    }

    public void logNative(LogLevel level, String tag, String message)
    {
        switch (level)
        {
            case VERBOSE:
                Log.v(tag, message);
                break;

            case DEBUG:
                Log.d(tag, message);
                break;

            case INFO:
                Log.i(tag, message);
                break;

            case WARNING:
                Log.w(tag, message);
                break;

            case ERROR:
                Log.e(tag, message);
                break;
        }
    }
}