package com.mauriciotogneri.androidutils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

public class FragmentBuilder<T extends Fragment>
{
    private final T fragment;
    private final Bundle parameters = new Bundle();

    public FragmentBuilder(Class<?> clazz)
    {
        this.fragment = instance(clazz);
    }

    public FragmentBuilder(T instance)
    {
        this.fragment = instance;
    }

    @SuppressWarnings("unchecked")
    private T instance(Class<?> clazz)
    {
        try
        {
            return (T) clazz.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void add(String key, String value)
    {
        parameters.putString(key, value);
    }

    public void add(String key, boolean value)
    {
        parameters.putBoolean(key, value);
    }

    public void add(String key, int value)
    {
        parameters.putInt(key, value);
    }

    public void add(String key, long value)
    {
        parameters.putLong(key, value);
    }

    public void add(String key, float value)
    {
        parameters.putFloat(key, value);
    }

    public void add(String key, double value)
    {
        parameters.putDouble(key, value);
    }

    public void add(String key, Serializable value)
    {
        parameters.putSerializable(key, value);
    }

    @SuppressWarnings("unchecked")
    public T build()
    {
        if ((parameters.size() != 0) && (fragment != null))
        {
            fragment.setArguments(parameters);
        }

        return fragment;
    }
}