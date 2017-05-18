package com.mauriciotogneri.androidutils;

import android.content.Intent;

public class ActivityParameters
{
    private final Intent intent;

    public ActivityParameters(Intent intent)
    {
        this.intent = intent;
    }

    private boolean hasParameters()
    {
        return (intent != null) && (intent.getExtras() != null);
    }

    public boolean hasParameter(String key)
    {
        return (hasParameters() && intent.getExtras().containsKey(key));
    }

    @SuppressWarnings("unchecked")
    public <T> T parameter(String key, T defaultValue)
    {
        if (hasParameter(key))
        {
            return (T) intent.getExtras().get(key);
        }
        else
        {
            return defaultValue;
        }
    }
}