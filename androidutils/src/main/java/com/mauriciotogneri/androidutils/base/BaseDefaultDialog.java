package com.mauriciotogneri.androidutils.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class BaseDefaultDialog<C> extends DialogFragment
{
    protected C callback;

    protected abstract String tag();

    @SuppressWarnings("unchecked")
    protected <T> T parameter(String key, T defaultValue)
    {
        Bundle extras = getArguments();

        if ((extras != null) && extras.containsKey(key))
        {
            return (T) extras.get(key);
        }
        else
        {
            return defaultValue;
        }
    }

    public void display(DialogDisplayer displayer)
    {
        try
        {
            FragmentManager fragmentManager = displayer.fragmentManager();
            Fragment previousDialog = fragmentManager.findFragmentByTag(tag());

            if ((previousDialog == null) && (!isAdded()))
            {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(this, tag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    public void close()
    {
        try
        {
            dismiss();
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    protected void post(Runnable runnable)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    protected void post(Runnable runnable, long delay)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delay);
    }

    // hack for android issue 17423 in the compatibility library
    @Override
    public void onDestroyView()
    {
        if ((getDialog() != null) && getRetainInstance())
        {
            getDialog().setDismissMessage(null);
        }

        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // no call for super(), bug on API Level > 11
    }
}