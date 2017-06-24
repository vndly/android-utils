package com.mauriciotogneri.androidutils.uibinder.containers;

import android.support.annotation.IdRes;
import android.view.View;

public class ViewContainer implements UiContainer
{
    private final View view;

    public ViewContainer(View view)
    {
        this.view = view;
    }

    @Override
    public View findViewById(@IdRes int id)
    {
        return view.findViewById(id);
    }
}