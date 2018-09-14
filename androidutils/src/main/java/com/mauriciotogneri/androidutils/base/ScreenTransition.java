package com.mauriciotogneri.androidutils.base;

import android.app.Activity;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.v4.app.FragmentTransaction;

public class ScreenTransition
{
    private final int enter;
    private final int exit;
    private final int popEnter;
    private final int popExit;

    public ScreenTransition(@AnimatorRes @AnimRes int enter,
                            @AnimatorRes @AnimRes int exit,
                            @AnimatorRes @AnimRes int popEnter,
                            @AnimatorRes @AnimRes int popExit)
    {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }

    public ScreenTransition(@AnimatorRes @AnimRes int enter,
                            @AnimatorRes @AnimRes int exit)
    {
        this(enter, exit, -1, -1);
    }

    public ScreenTransition()
    {
        this(-1, -1, -1, -1);
    }

    public void setupFragment(FragmentTransaction transaction)
    {
        if ((enter != 1) && (exit != -1) && (popEnter != -1) && (popExit != -1))
        {
            transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        }
        else if ((enter != 1) && (exit != -1))
        {
            transaction.setCustomAnimations(enter, exit);
        }
    }

    public void setupActivityEnter(Activity activity)
    {
        if ((enter != 1) && (exit != -1))
        {
            activity.overridePendingTransition(enter, exit);
        }
    }

    public void setupActivityExit(Activity activity)
    {
        if ((popEnter != 1) && (popExit != -1))
        {
            activity.overridePendingTransition(popEnter, popExit);
        }
    }
}