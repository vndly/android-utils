package com.mauriciotogneri.androidutils;

import android.os.Bundle;

public class FragmentParameters
{
    private final Bundle bundle;

    public FragmentParameters(Bundle bundle)
    {
        this.bundle = bundle;
    }

    private boolean hasParameters()
    {
        return (bundle != null);
    }

    public boolean hasParameter(String key)
    {
        return (hasParameters() && bundle.containsKey(key));
    }

    @SuppressWarnings("unchecked")
    public <T> T parameter(String key, T defaultValue)
    {
        if (hasParameter(key))
        {
            return (T) bundle.get(key);
        }
        else
        {
            return defaultValue;
        }
    }
}