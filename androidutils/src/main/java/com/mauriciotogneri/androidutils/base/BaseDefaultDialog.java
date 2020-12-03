package com.mauriciotogneri.androidutils.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseDefaultDialog<C> extends DialogFragment
{
    protected C callback;

    protected abstract String tag();

    protected boolean openKeyboard()
    {
        return false;
    }

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (openKeyboard())
        {
            try
            {
                Window window = getDialog().getWindow();

                if (window != null)
                {
                    window.setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
            }
            catch (Exception e)
            {
                // ignore
            }
        }
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