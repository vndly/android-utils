package com.mauriciotogneri.androidutils.uibinder.containers;

import android.app.Activity;
import android.view.View;

import androidx.annotation.IdRes;

public class ActivityContainer implements UiContainer
{
    private final Activity activity;

    public ActivityContainer(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public View findViewById(@IdRes int id)
    {
        return activity.findViewById(id);
    }
}